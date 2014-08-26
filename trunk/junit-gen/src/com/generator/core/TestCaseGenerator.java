package com.generator.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.generator.core.comparators.ComplexityComparator;
import com.generator.core.util.Log;

public class TestCaseGenerator {

	private static final long FEEDBACK_INTERVAL = 1000;

	final JUnitGenerator jUnitGenerator;
	private final Method method;
	private final CaseExecutor executor;

	public TestCaseGenerator(JUnitGenerator jUnitGenerator, Method method) {
		this.jUnitGenerator = jUnitGenerator;
		this.method = method;
		this.executor = new CaseExecutor(jUnitGenerator, method);
	}

	public List<TestCaseData> execute() {

		ValueGeneratorRegistry registry = jUnitGenerator.getValueGenerators();
		ParamValuesGenerator paramValuesGen = new ParamValuesGenerator(registry, method.getParameterTypes());
		int minTestCases = jUnitGenerator.getMinTestCasesPerMethod();

		// TODO Turn this guys into parameters:
		int minTries = 200;
		int maxTries = 20000;
		int clearTrigger = 200;

		List<TestCaseData> result = new ArrayList<>();
		int tries = 0;
		double[] covRatios = null;
		for (;;) {
			showFeedbackMsg(tries, result, covRatios);

			if (!paramValuesGen.hasNext()) {
				Log.warning(String.format("Reached limit of inputs for method '%s' (%d inputs).", method.getName(),
						tries));
				break;
			}
			if (tries >= minTries && result.size() > minTestCases && isEnoughCoverage(result)) {
				break;
			}
			if (result.size() > clearTrigger) {
				covRatios = removeRedundantTestCases(result, minTestCases);
			}
			if (tries >= maxTries) {
				Log.warning("Reached limit of attempts for method '" + method.getName() + "' (" + tries + ").");
				break;
			}
			++tries;
			List<Object> paramValues = paramValuesGen.next();
			ExecutionResult execResult = executor.executeCoverage(paramValues);
			if (!isTestCaseRelevant(execResult)) {
				continue;
			}
			TestCaseData caseData = new TestCaseData(paramValues, execResult);
			result.add(caseData);
		}
		covRatios = removeRedundantTestCases(result, minTestCases);
		Log.info(String.format("Final coverages: %s. Tries: %d.", coveragesToStr(covRatios), tries));
		return result;
	}

	private long lastFeedback = 0L;

	private void showFeedbackMsg(int tries, List<TestCaseData> result, double[] covRatios) {
		long now = System.currentTimeMillis();
		if (tries == 0) {
			lastFeedback = now;
			return;
		}
		if (now < lastFeedback + FEEDBACK_INTERVAL) {
			return;
		}
		lastFeedback = now;
		String msg = "Working. %d tries. %d current cases. Coverages: %s.";
		Log.info(String.format(msg, tries, result.size(), coveragesToStr(covRatios)));
	}

	private String coveragesToStr(double[] covRatios) {
		StringBuilder strCov = new StringBuilder();
		if (covRatios == null) {
			strCov.append("0.0%");
		} else {
			for (double cr : covRatios) {
				if (strCov.length() > 0) {
					strCov.append(", ");
				}
				strCov.append(String.format("%.1f%%", cr * 100));
			}
		}
		return strCov.toString();
	}

	private double[] removeRedundantTestCases(List<TestCaseData> cases, int minTestCases) {
		if (cases.isEmpty()) {
			return null;
		}
		int maxCovDepth = jUnitGenerator.getMaxCoverageDepth();
		// Sort the test cases (higher coverages / low complexity come first):
		Collections.sort(cases, new TestCaseComparator(maxCovDepth));

		Iterator<TestCaseData> iterator = cases.iterator();
		TestCaseData firstCase = iterator.next();
		if (!firstCase.getResult().hasCoverageInfo()) {
			return null;
		}
		CoverageInfo totalCoverage = firstCase.getResult().getCoverageInfo().copy();

		List<TestCaseData> removedOnes = new ArrayList<>();
		while (iterator.hasNext()) {
			TestCaseData testCase = iterator.next();
			ExecutionResult execRes = testCase.getResult();
			if (!execRes.hasCoverageInfo()) {
				iterator.remove();
				removedOnes.add(testCase);
				continue;
			}
			CoverageInfo after = CoverageInfo.merge(execRes.getCoverageInfo(), totalCoverage);
			if (!coversSomethingMore(after, totalCoverage)) {
				iterator.remove();
				removedOnes.add(testCase);
				continue;
			}
			totalCoverage = after;
		}
		int i = 0;
		while (cases.size() < minTestCases && i < removedOnes.size()) {
			cases.add(removedOnes.get(i++));
		}
		return mountCoverageRatios(totalCoverage, maxCovDepth);
	}

	private double[] mountCoverageRatios(CoverageInfo totalCoverage, int maxCovDepth) {
		double[] res = new double[maxCovDepth];
		for (int i = 0; i < res.length; ++i) {
			res[i] = totalCoverage.getDeepCounter(i).getRatio();
		}
		return res;
	}

	private boolean coversSomethingMore(CoverageInfo after, CoverageInfo total) {
		if (after.getShallowCounter().greaterThan(total.getShallowCounter())) {
			return true;
		}
		int maxDepth = jUnitGenerator.getMaxCoverageDepth();
		for (int i = 0; i < maxDepth; ++i) {
			if (after.getDeepCounter(i).greaterThan(total.getDeepCounter(i))) {
				return true;
			}
		}
		return false;
	}

	private static class TestCaseComparator implements Comparator<TestCaseData> {

		private final Comparator<Object> complexityComparator = new ComplexityComparator();
		private final int maxCoverageDepth;

		public TestCaseComparator(int maxCoverageDepth) {
			this.maxCoverageDepth = maxCoverageDepth;
		}

		@Override
		public int compare(TestCaseData o1, TestCaseData o2) {
			ExecutionResult r1 = o1.getResult();
			ExecutionResult r2 = o2.getResult();
			int res = Boolean.compare(!r1.hasCoverageInfo(), !r2.hasCoverageInfo());
			if (res != 0) {
				return res;
			}
			res = r2.getCoverageInfo().compareCoverages(r1.getCoverageInfo(), maxCoverageDepth);
			if (res != 0) {
				return res;
			}
			res = Boolean.compare(r1.failed(), r2.failed());
			if (res != 0) {
				return res;
			}
			// For cases with same coverage, the winner is the simpler:
			return compareValuesComplexity(o1.getParamValues(), o2.getParamValues());
		}

		private int compareValuesComplexity(List<Object> v1, List<Object> v2) {
			int sum = 0;
			int firstDiff = 0;
			for (int i = 0; i < v1.size(); ++i) {
				int comp = complexityComparator.compare(v1.get(i), v2.get(i));
				int normalized = comp == 0 ? 0 : comp < 0 ? -1 : 1;
				if (firstDiff == 0 && normalized != 0) {
					firstDiff = normalized;
				}
				sum += normalized;
			}
			if (sum != 0) {
				return sum;
			}
			return firstDiff;
		}

	}

	// FIXME Problem if test case has no coverage info!
	private boolean isEnoughCoverage(List<TestCaseData> cases) {

		CoverageInfo[] infos = new CoverageInfo[cases.size()];
		for (int i = 0; i < infos.length; ++i) {
			infos[i] = cases.get(i).getResult().getCoverageInfo();
		}
		CoverageInfo merged = CoverageInfo.merge(infos);
		double[] minCovRatio = jUnitGenerator.getMinCovRatioPerMethod();
		for (int i = 0; i < minCovRatio.length; ++i) {
			if (merged.getDeepCounter(i).getRatio() < minCovRatio[i]) {
				return false;
			}
		}
		return true;
	}

	// TODO Extract these rules to an extensible interface:
	private boolean isTestCaseRelevant(ExecutionResult execResult) {
		double minCovSucceeded = jUnitGenerator.getMinCovRatioPerSucceededTestCase();
		double minCovFailed = jUnitGenerator.getMinCovRatioPerFailedTestCase();
		if (execResult.hasCoverageInfo()) {
			if (execResult.succeeded() && execResult.getShallowCoverageRatio() < minCovSucceeded) {
				return false;
			}
			if (execResult.failed() && execResult.getShallowCoverageRatio() < minCovFailed) {
				return false;
			}
		} else {
			Log.warning("Test case has no coverage information.");
		}
		if (execResult.failed()) {
			switch (jUnitGenerator.getExceptionsStrategy()) {
				case IGNORE_ALWAYS:
					return false;
				case IGNORE_WHEN_NOT_DECLARED:
					if (!execResult.isExceptionDeclared()) {
						return false;
					}
					break;
				default:
					break;
			}
		}
		return true;
	}

}
