package com.generator.structure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.generator.structure.util.Log;

public class TestCaseGenerator {

	private JUnitGenerator jUnitGenerator;
	private final Method method;
	private final InnerExecutor executor;

	public TestCaseGenerator(JUnitGenerator jUnitGenerator, Method method) {
		this.jUnitGenerator = jUnitGenerator;
		this.method = method;
		this.executor = new InnerExecutor(method);
	}

	public List<TestCaseData> execute() {

		ValueGeneratorRegistry registry = jUnitGenerator.getValueGenerators();
		ValueSetGenerator paramValuesGen = new ValueSetGenerator(registry, method.getParameterTypes());
		int minTestCases = jUnitGenerator.getMinTestCasesPerMethod();

		// TODO Turn this guys into parameters:
		int minTries = 200;
		int maxTries = 20000;
		int clearTrigger = 200;

		List<TestCaseData> result = new ArrayList<>();
		int tries = 0;
		for (;;) {
			if (!paramValuesGen.hasNext()) {
				Log.warning(String.format("Reached limit of inputs for method '%s' (%d inputs).", method.getName(),
						tries));
				break;
			}
			if (tries > minTries && result.size() > minTestCases && isEnoughCoverage(result)) {
				break;
			}
			if (result.size() > clearTrigger) {
				double covRat = removeRedundantTestCases(result, minTestCases);
				Log.info(String.format("It's being hard. %d tries. %d relevant cases. Coverage: %.2f%%.", tries,
						result.size(), covRat * 100));
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
		double covRat = removeRedundantTestCases(result, minTestCases);
		Log.info(String.format("Final coverage: %.2f%%. Tries: %d.", covRat * 100, tries));
		return result;
	}

	private double removeRedundantTestCases(List<TestCaseData> cases, int minTestCases) {
		if (cases.isEmpty()) {
			return 0.0;
		}
		// Sort the test cases (higher coverages / low complexity come first):
		Collections.sort(cases, new TestCaseComparator());

		Iterator<TestCaseData> iterator = cases.iterator();
		TestCaseData firstCase = iterator.next();
		if (!firstCase.getResult().hasCoverageInfo()) {
			return 0.0;
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
			if (after.getCoverageRatio() <= totalCoverage.getCoverageRatio()) {
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
		return totalCoverage.getCoverageRatio();
	}

	private static class TestCaseComparator implements Comparator<TestCaseData> {

		private Comparator<String> strComparator = new StringComplexityComparator();

		@Override
		public int compare(TestCaseData o2, TestCaseData o1) {
			ExecutionResult r1 = o1.getResult();
			ExecutionResult r2 = o2.getResult();
			int res = Boolean.compare(r1.hasCoverageInfo(), r2.hasCoverageInfo());
			if (res != 0) {
				return res;
			}
			CoverageInfo cov1 = r1.getCoverageInfo();
			CoverageInfo cov2 = r2.getCoverageInfo();
			res = Double.compare(cov1.getCoverageRatio(), cov2.getCoverageRatio());
			if (res != 0) {
				return res;
			}
			res = Boolean.compare(r1.succeeded(), r2.succeeded());
			if (res != 0) {
				return res;
			}
			// For cases with same coverage, the winner is the simpler:
			return compareValuesComplexity(o2.getParamValues(), o1.getParamValues());
		}

		private int compareValuesComplexity(List<Object> v1, List<Object> v2) {
			int res = Integer.compare(v1.size(), v2.size());
			if (res != 0) {
				return res;
			}
			for (int i = 0; i < v1.size(); ++i) {
				res += compareComplexity(v1.get(i), v2.get(i));
			}
			return res;
		}

		private int compareComplexity(Object o1, Object o2) {
			if (o1 == null || o2 == null) {
				return Boolean.compare(o1 != null, o2 != null);
			}
			if (o1 instanceof Number && o1 instanceof Comparable) {
				// Prefers the closest to zero:
				double d1 = ((Number) o1).doubleValue();
				double d2 = ((Number) o2).doubleValue();
				int res = Double.compare(Math.abs(d1), Math.abs(d2));
				if (res != 0) {
					return res;
				}
				// If abs are equal, maybe one is negative and other is
				// positive. In this case, prefer the positive.
				return Double.compare(d2, d1);
			}
			if (o1 instanceof String) {
				return strComparator.compare((String) o1, (String) o2);
			}
			if (o1 instanceof Enum) {
				return 0;
			}
			if (o1 instanceof Boolean) {
				return 0;
			}
			if (o1 instanceof Character) {
				// TODO Prefer numbers than letters than special chars
				return 0;
			}
			Log.warning("Don't know how to compare elements of type " + o1.getClass().getSimpleName() + ".");
			return 0;
		}

	}

	// FIXME Problem if test case has no coverage info!
	private boolean isEnoughCoverage(List<TestCaseData> cases) {

		CoverageInfo[] infos = new CoverageInfo[cases.size()];
		for (int i = 0; i < infos.length; ++i) {
			infos[i] = cases.get(i).getResult().getCoverageInfo();
		}
		CoverageInfo merged = CoverageInfo.merge(infos);
		return merged.getCoverageRatio() >= jUnitGenerator.getMinCovRatioPerMethod();
	}

	// TODO Extract these rules to an extensible interface:
	private boolean isTestCaseRelevant(ExecutionResult execResult) {
		double minCovSucceeded = jUnitGenerator.getMinCovRatioPerSucceededTestCase();
		double minCovFailed = jUnitGenerator.getMinCovRatioPerFailedTestCase();
		if (execResult.hasCoverageInfo()) {
			if (execResult.succeeded() && execResult.getCoverageRatio() < minCovSucceeded) {
				return false;
			}
			if (execResult.failed() && execResult.getCoverageRatio() < minCovFailed) {
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
