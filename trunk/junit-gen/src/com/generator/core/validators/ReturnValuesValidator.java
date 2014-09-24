package com.generator.core.validators;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.generator.core.ExecutionResult;
import com.generator.core.JUnitGenerator;
import com.generator.core.TestCaseData;
import com.generator.core.TestCaseValidator;

public class ReturnValuesValidator implements TestCaseValidator {

	// Represent a null value that can be used as a map key:
	private static final Object NULL = new Object();

	private final Method method;
	private final JUnitGenerator generator;

	private final Set<Object> expectedReturns;
	private final Set<Object> missingReturns = new HashSet<>();
	private final Map<Object, Set<TestCaseData>> casesByReturn = new HashMap<>();

	public ReturnValuesValidator(Method method, JUnitGenerator generator) {
		this.method = method;
		this.generator = generator;
		this.expectedReturns = getPossibleReturnValues();
		this.missingReturns.addAll(this.expectedReturns);
	}

	private Set<Object> getPossibleReturnValues() {
		HashSet<Object> res = new HashSet<>();

		Class<?> type = method.getReturnType();

		if (type.equals(boolean.class)) {
			res.add(false);
			res.add(true);
			
		} else if (type.equals(Boolean.class)) {
			res.add(NULL);
			res.add(false);
			res.add(true);
			
		} else if (type.isEnum()) {
			res.add(NULL);
			Object[] consts = type.getEnumConstants();
			for (Object obj : consts) {
				res.add(obj);
			}
		}

		// TODO Create a registry for these guys.
		// TODO Support more types.

		return res;
	}

	@Override
	public List<TestCaseData> include(TestCaseData testCase) {
		ExecutionResult execResult = testCase.getResult();
		if (!execResult.failed()) {
			Object returnValue = normalize(execResult.getResult());
			Set<TestCaseData> cases = casesByReturn.get(returnValue);
			if (cases == null) {
				cases = new HashSet<>();
				casesByReturn.put(returnValue, cases);
			}
			cases.add(testCase);
			missingReturns.remove(returnValue);
		}
		return Collections.emptyList();
	}

	private Object normalize(Object result) {
		if (result == null) {
			return NULL;
		} else {
			return result;
		}
	}

	@Override
	public boolean canBeRemoved(TestCaseData testCase) {
		if (expectedReturns.isEmpty()) {
			return true;
		}
		ExecutionResult execResult = testCase.getResult();
		if (execResult.failed()) {
			return true;
		} else {
			Object returnValue = normalize(execResult.getResult());
			Set<TestCaseData> cases = casesByReturn.get(returnValue);
			if (cases == null) {
				return true; // TODO Review!
			}
			if (!cases.contains(testCase)) {
				return true; // TODO Review!
			}
			// If this is the only case for this return value, it cannot be
			// removed.
			return cases.size() > 1;
		}
	}

	@Override
	public void remove(TestCaseData testCase) {
		ExecutionResult execResult = testCase.getResult();
		if (!execResult.failed()) {
			Object returnValue = normalize(execResult.getResult());
			Set<TestCaseData> cases = casesByReturn.get(returnValue);
			if (cases != null) {
				boolean removed = cases.remove(testCase);
				if (removed && cases.isEmpty()) {
					if (expectedReturns.contains(returnValue)) {
						missingReturns.add(returnValue);
					}
				}
			}
		}
	}

	@Override
	public boolean isSatisfied() {
		return missingReturns.isEmpty();
	}

	@Override
	public int compare(TestCaseData c1, TestCaseData c2) {
		// TODO Is this correct?
		// All cases have the same importance.
		return 0;
	}

	@Override
	public String getFeedbackMessage() {
		int all = expectedReturns.size();
		int cov = all - missingReturns.size();
		return String.format("Returns: %d/%d.", cov, all);
	}

}
