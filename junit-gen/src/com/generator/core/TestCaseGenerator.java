package com.generator.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.generator.core.util.Log;
import com.generator.core.validators.CoverageValidator;

public class TestCaseGenerator {

	private static final long FEEDBACK_INTERVAL = 1000;

	final JUnitGenerator jUnitGenerator;
	private final Method method;
	private final CaseExecutor executor;
	private final List<CoverageValidator> validators;

	public TestCaseGenerator(JUnitGenerator jUnitGenerator, Method method) {
		this.jUnitGenerator = jUnitGenerator;
		this.method = method;
		this.executor = new CaseExecutor(jUnitGenerator, method);

		validators = new ArrayList<>();
		validators.add(new CoverageValidator(jUnitGenerator));
	}

	public List<TestCaseData> execute() {

		ValueGeneratorRegistry registry = jUnitGenerator.getValueGenerators();
		ParamValuesGenerator paramValuesGen = new ParamValuesGenerator(registry, method.getParameterTypes());
		int minTestCases = jUnitGenerator.getMinTestCasesPerMethod();

		// TODO Turn this guys into parameters:
		int minTries = 200;
		int maxTries = 20000;

		List<TestCaseData> cases = new ArrayList<>();
		int tries = 0;
		for (;;) {
			if (!paramValuesGen.hasNext()) {
				Log.warning(String.format("Reached limit of inputs for method '%s' (%d inputs).", method.getName(),
						tries));
				break;
			}
			List<Object> paramValues = paramValuesGen.next();
			ExecutionResult execResult = executor.executeCoverage(paramValues);
			if (isTestCaseRelevant(execResult)) {
				TestCaseData caseData = new TestCaseData(paramValues, execResult);
				if (tries >= minTries && cases.size() >= minTestCases && isSatisfied(cases)) {
					break;
				}
				addCase(cases, caseData);
			}
			++tries;
			if (tries >= maxTries) {
				Log.warning(String.format("Reached limit of attempts for method '%s' (%d).", method.getName(), tries));
				break;
			}
			showFeedbackMsg(tries, cases);
		}
		sortTestCases(cases);
		Log.info(String.format("End. Tries: %d. %s", tries, getValidatorsFeedback()));
		return cases;
	}

	private void sortTestCases(List<TestCaseData> cases) {
		Collections.sort(cases, new Comparator<TestCaseData>() {

			@Override
			public int compare(TestCaseData o1, TestCaseData o2) {
				for (TestCaseValidator val : validators) {
					int res = val.compare(o1, o2);
					if (res != 0) {
						return res;
					}
				}
				return 0;
			}
		});
	}

	private void addCase(List<TestCaseData> result, TestCaseData caseData) {
		result.add(caseData);

		List<List<TestCaseData>> redundants = new ArrayList<>();
		for (TestCaseValidator validator : validators) {
			redundants.add(validator.include(caseData));
		}

		// Remove redundant test cases.
		// A test case is considered redundant only if all validators considered
		// it redundant.
		if (!redundants.isEmpty()) {
			List<TestCaseData> reallyRedundants = redundants.get(0);
			for (int i = 1; i < redundants.size(); ++i) {
				reallyRedundants.retainAll(redundants.get(i));
			}
			if (!reallyRedundants.isEmpty()) {
				int minCases = jUnitGenerator.getMinTestCasesPerMethod();
				if (result.size() > minCases) {
					removeLast(result, reallyRedundants, result.size() - minCases);
				}
			}
		}
	}

	private void removeLast(List<TestCaseData> result, List<TestCaseData> redundants, int count) {
		for (int i = redundants.size() - 1; i >= 0 && count > 0; --i, --count) {
			TestCaseData testCase = redundants.get(i);
			result.remove(testCase);
			for (TestCaseValidator val : validators) {
				if (!val.remove(testCase)) {
					Log.error("Remove returned false! Validator: " + val.getClass().getSimpleName());
				}
			}
		}
	}

	// Returns true only when all validators are satisfied.
	private boolean isSatisfied(List<TestCaseData> result) {
		for (TestCaseValidator validator : validators) {
			if (!validator.isSatisfied()) {
				return false;
			}
		}
		return true;
	}

	private long lastFeedback = 0L;

	private void showFeedbackMsg(int tries, List<TestCaseData> result) {
		long now = System.currentTimeMillis();
		if (tries <= 1) {
			lastFeedback = now;
			return;
		}
		if (now < lastFeedback + FEEDBACK_INTERVAL) {
			return;
		}
		lastFeedback = now;

		String fb = getValidatorsFeedback();
		Log.info(String.format("Working. %d tries. %d current cases. %s", tries, result.size(), fb));
	}

	private String getValidatorsFeedback() {
		StringBuilder sb = new StringBuilder();
		for (TestCaseValidator val : validators) {
			if (sb.length() != 0) {
				sb.append(" ");
			}
			sb.append(val.getFeedbackMessage());
		}
		return sb.toString();
	}

	private boolean isTestCaseRelevant(ExecutionResult execResult) {
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
