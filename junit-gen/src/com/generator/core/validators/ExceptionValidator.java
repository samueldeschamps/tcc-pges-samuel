package com.generator.core.validators;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.generator.core.ExecutionResult;
import com.generator.core.TestCaseData;
import com.generator.core.TestCaseValidator;

public class ExceptionValidator implements TestCaseValidator {

	private Map<ExKey, Set<TestCaseData>> map = new HashMap<>();

	@Override
	public List<TestCaseData> include(TestCaseData testCase) {
		ExecutionResult testResult = testCase.getResult();
		if (!testResult.failed()) {
			return Collections.singletonList(testCase);
		}
		addCase(testCase);
		return Collections.emptyList();
	}

	private void addCase(TestCaseData testCase) {
		ExKey key = ExKey.fromTestCase(testCase);
		Set<TestCaseData> cases = map.get(key);
		if (cases == null) {
			cases = new HashSet<>();
			map.put(key, cases);
		}
		cases.add(testCase);
	}

	@Override
	public boolean canBeRemoved(TestCaseData testCase) {
		if (!testCase.getResult().failed()) {
			return true;
		}
		ExKey key = ExKey.fromTestCase(testCase);
		Set<TestCaseData> cases = map.get(key);
		if (cases == null || !cases.contains(key)) {
			return true;
		}
		return cases.size() > 1;
	}

	@Override
	public void remove(TestCaseData testCase) {
		if (testCase.getResult().failed()) {
			ExKey key = ExKey.fromTestCase(testCase);
			Set<TestCaseData> cases = map.get(key);
			if (cases != null) {
				cases.remove(testCase);
				if (cases.isEmpty()) {
					map.remove(key);
				}
			}
		}
	}

	@Override
	public boolean isSatisfied() {
		// Always satisfied.
		return true;
	}

	@Override
	public int compare(TestCaseData c1, TestCaseData c2) {
		ExecutionResult r1 = c1.getResult();
		ExecutionResult r2 = c2.getResult();
		return Boolean.compare(r1.succeeded(), r2.succeeded());
	}

	@Override
	public String getFeedbackMessage() {
		return String.format("Exceptions: %d.", map.size());
	}

	private static class ExKey {

		private final Class<?> clazz;

		public ExKey(Class<?> clazz) {
			this.clazz = clazz;
		}

		public static ExKey fromTestCase(TestCaseData testCase) {
			ExecutionResult testRes = testCase.getResult();
			return new ExKey(testRes.getExceptionClass());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof ExKey)) {
				return false;
			}
			ExKey other = (ExKey) obj;
			return this.clazz == other.clazz;
		}

		@Override
		public int hashCode() {
			int res = clazz.hashCode();
			return res;
		}
	}

}
