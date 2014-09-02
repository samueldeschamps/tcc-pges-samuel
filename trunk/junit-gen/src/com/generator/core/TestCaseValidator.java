package com.generator.core;

import java.util.List;

public interface TestCaseValidator {

	/**
	 * Includes a new generated test case and returns test cases that are
	 * redudant after the inclusion of this one.<br>
	 * The return can be the new test case, if it's redundant.<br>
	 * Important: The return list must be sorted from the less redundant to the
	 * most redundant.
	 */
	public List<TestCaseData> include(TestCaseData testCase);

	/**
	 * Returns whether this case can be removed or not.<br>
	 * Usually a case can be removed if it still has a substitute case.
	 */
	public boolean canBeRemoved(TestCaseData testCase);

	/**
	 * Removes this test case from this validator.
	 */
	public void remove(TestCaseData testCase);

	public boolean isSatisfied();

	public int compare(TestCaseData c1, TestCaseData c2);

	// public int compare(TestCaseData c1, TestCaseData c2);

	// public boolean isRedundant(TestCaseData testCase);

	public String getFeedbackMessage();

}
