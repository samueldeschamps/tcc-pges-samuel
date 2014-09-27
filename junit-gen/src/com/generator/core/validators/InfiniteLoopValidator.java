package com.generator.core.validators;

import java.util.Collections;
import java.util.List;

import com.generator.core.TestCaseData;
import com.generator.core.TestCaseValidator;

public class InfiniteLoopValidator implements TestCaseValidator {

	private TestCaseData infiniteLoopCase = null;

	@Override
	public List<TestCaseData> include(TestCaseData testCase) {
		if (infiniteLoopCase == null && testCase.getResult().isInfiniteLoop()) {
			infiniteLoopCase = testCase;
			return Collections.emptyList();
		} else {
			return Collections.singletonList(testCase);
		}
	}

	@Override
	public boolean canBeRemoved(TestCaseData testCase) {
		return testCase != infiniteLoopCase && testCase != null;
	}

	@Override
	public void remove(TestCaseData testCase) {
		if (testCase == infiniteLoopCase) {
			infiniteLoopCase = null;
		}
	}

	@Override
	public boolean isSatisfied() {
		return true;
	}

	@Override
	public int compare(TestCaseData c1, TestCaseData c2) {
		return Boolean.compare(c2.getResult().isInfiniteLoop(), c1.getResult().isInfiniteLoop());
	}

	@Override
	public String getFeedbackMessage() {
		if (infiniteLoopCase != null) {
			return "Infinite loop found!";
		} else {
			return "";
		}
	}

}
