package com.generator.core.validators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.generator.core.CoverageInfo;
import com.generator.core.ExecutionResult;
import com.generator.core.JUnitGenerator;
import com.generator.core.TestCaseComparator;
import com.generator.core.TestCaseData;
import com.generator.core.TestCaseValidator;

public class CoverageValidator implements TestCaseValidator {

	private final JUnitGenerator generator;
	private final TreeSet<TestCaseData> cases;
	private final TestCaseComparator comparator;
	private CoverageInfo totalCov = null;

	public CoverageValidator(JUnitGenerator generator) {
		this.generator = generator;
		this.comparator = new TestCaseComparator(generator.getMaxCoverageDepth());
		this.cases = new TreeSet<>(comparator);
	}

	@Override
	public List<TestCaseData> include(TestCaseData testCase) {
		int maxCovDepth = generator.getMaxCoverageDepth();

		cases.add(testCase);

		Iterator<TestCaseData> iterator = cases.iterator();
		TestCaseData firstCase = iterator.next();
		CoverageInfo newTotalCov = firstCase.getResult().getCoverageInfo().copy();

		List<TestCaseData> redundants = new ArrayList<>();
		while (iterator.hasNext()) {
			TestCaseData currCase = iterator.next();
			ExecutionResult execRes = currCase.getResult();
			CoverageInfo after = CoverageInfo.merge(execRes.getCoverageInfo(), newTotalCov);
			if (!coversSomethingMore(after, newTotalCov, maxCovDepth)) {
				redundants.add(currCase);
			} else {
				newTotalCov = after;
			}
		}
		totalCov = newTotalCov;
		return redundants;
	}

	private boolean coversSomethingMore(CoverageInfo after, CoverageInfo total, int maxDepth) {
		for (int i = 0; i < maxDepth; ++i) {
			if (after.getDeepCounter(i).greaterThan(total.getDeepCounter(i))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(TestCaseData testCase) {
		return cases.remove(testCase);
	}

	@Override
	public boolean isSatisfied() {
		if (totalCov == null) {
			return false;
		}
		double[] minCovRatio = generator.getMinCovRatioPerMethod();
		for (int i = 0; i < minCovRatio.length; ++i) {
			if (totalCov.getDeepCounter(i).getRatio() < minCovRatio[i]) {
				return false;
			}
		}
		return true;
	}

	private double[] getCovRatios() {
		double[] res = new double[generator.getMaxCoverageDepth()];
		for (int i = 0; i < res.length; ++i) {
			res[i] = totalCov.getDeepCounter(i).getRatio();
		}
		return res;
	}

	@Override
	public String getFeedbackMessage() {
		return String.format("Coverages: %s.", coveragesToStr());
	}

	private String coveragesToStr() {
		StringBuilder strCov = new StringBuilder();
		if (totalCov == null) {
			strCov.append("0.0%");
		} else {
			double[] covRatios = getCovRatios();
			for (double cr : covRatios) {
				if (strCov.length() > 0) {
					strCov.append(", ");
				}
				strCov.append(String.format("%.1f%%", cr * 100));
			}
		}
		return strCov.toString();
	}

	@Override
	public int compare(TestCaseData c1, TestCaseData c2) {
		return comparator.compare(c1, c2);
	}

}
