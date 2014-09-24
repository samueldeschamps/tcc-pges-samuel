package com.generator.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.generator.core.util.Log;
import com.generator.core.validators.CoverageValidator;
import com.generator.core.validators.ExceptionValidator;
import com.generator.core.validators.ReturnValuesValidator;

public class TestCaseGenerator {

	private static final long FEEDBACK_INTERVAL = 1000;

	final JUnitGenerator jUnitGenerator;
	private final Method method;
	private final CaseExecutor executor;
	private final List<TestCaseValidator> validators;

	public TestCaseGenerator(JUnitGenerator jUnitGenerator, Method method) {
		this.jUnitGenerator = jUnitGenerator;
		this.method = method;
		this.executor = new CaseExecutor(jUnitGenerator, method);

		validators = new ArrayList<>();
		validators.add(new CoverageValidator(jUnitGenerator));
		validators.add(new ReturnValuesValidator(method, jUnitGenerator));
		validators.add(new ExceptionValidator());
	}

	public List<TestCaseData> execute() {

		ValueGeneratorRegistry registry = jUnitGenerator.getValueGenerators();
		ParamValuesGenerator paramValuesGen = new ParamValuesGenerator(registry, method);
		int minTestCases = jUnitGenerator.getMinTestCasesPerMethod();

		// TODO Turn this guys into parameters:
		int minTries = 200;
		int maxTries = 20000;

		List<TestCaseData> cases = new LinkedList<>();
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
				if (tries >= minTries && cases.size() >= minTestCases && isSatisfied()) {
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
		Collections.sort(cases, testCaseComparator);
		Log.info(String.format("End. Tries: %d. %s", tries, getValidatorsFeedback()));
		return cases;
	}

	private final Comparator<TestCaseData> testCaseComparator = new Comparator<TestCaseData>() {
		
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
	};
	
	private void addCase(List<TestCaseData> result, TestCaseData caseData) {
		result.add(caseData);

		List<List<TestCaseData>> redundantLists = new ArrayList<>();
		Set<TestCaseData> redundants = new LinkedHashSet<>();
		for (TestCaseValidator validator : validators) {
			List<TestCaseData> leftover = validator.include(caseData);
			redundantLists.add(leftover);
			redundants.addAll(leftover);
		}
		
		// Remove redundant test cases.
		// A test case is considered redundant only if all validators considered
		// it redundant.
		if (!redundantLists.isEmpty()) {
			// Remove cases that are redundant for all validators:
			List<TestCaseData> reallyRedundants = new ArrayList<>(redundantLists.get(0));
			for (int i = 1; i < redundantLists.size(); ++i) {
				reallyRedundants.retainAll(redundantLists.get(i));
			}
			int minCases = jUnitGenerator.getMinTestCasesPerMethod();
			if (!reallyRedundants.isEmpty() && result.size() > minCases) {
				int qtdToRemove = result.size() - minCases;
				for (int i = reallyRedundants.size() - 1; i >= 0 && qtdToRemove > 0; --i, --qtdToRemove) {
					TestCaseData testCase = reallyRedundants.get(i);
					result.remove(testCase);
					redundants.remove(testCase);
					removeAtAll(testCase);
				}
			}

			// Remove cases that can be redundant for all validators:
			if (result.size() > minCases) {
				Object[] array = redundants.toArray();
				for (int i = array.length - 1; i >= 0; --i) {
					TestCaseData testCase = (TestCaseData) array[i];
					if (canBeRemovedAtAll(testCase, redundantLists)) {
						result.remove(testCase);
						removeAtAll(testCase);
					}
					if (result.size() <= minCases) {
						break;
					}
				}
			}
		}
	}

	private void removeAtAll(TestCaseData testCase) {
		for (TestCaseValidator val : validators) {
			val.remove(testCase);
		}
	}

	private boolean canBeRemovedAtAll(TestCaseData testCase, List<List<TestCaseData>> redundantLists) {
		for (int i = 0; i < validators.size(); ++i) {
			TestCaseValidator val = validators.get(i);
			if (redundantLists.get(i).contains(testCase)) {
				// This validator has already told this case was redundant.
				continue;
			}
			if (!val.canBeRemoved(testCase)) {
				return false;
			}
		}
		return true;
	}

	// Returns true only when all validators are satisfied.
	private boolean isSatisfied() {
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
