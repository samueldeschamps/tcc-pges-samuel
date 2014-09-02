package com.generator.core;

import java.util.Comparator;
import java.util.List;

import com.generator.core.comparators.ComplexityComparator;

public class TestCaseComparator implements Comparator<TestCaseData> {

	private final Comparator<Object> complexityComparator = new ComplexityComparator();
	private final int maxCoverageDepth;

	public TestCaseComparator(int maxCoverageDepth) {
		this.maxCoverageDepth = maxCoverageDepth;
	}

	@Override
	public int compare(TestCaseData o1, TestCaseData o2) {
		ExecutionResult r1 = o1.getResult();
		ExecutionResult r2 = o2.getResult();
		int res = r2.getCoverageInfo().compareCoverages(r1.getCoverageInfo(), maxCoverageDepth);
		if (res != 0) {
			return res;
		}
		// For cases with same coverage, the winner is the simpler:
		res = compareValuesComplexity(o1.getParamValues(), o2.getParamValues());
		if (res != 0) {
			return res;
		}
		// If two different objects return compare == 0, the TreeSet thinks they
		// are the same and they replace one another.
		return Long.compare(o1.getId(), o2.getId());
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