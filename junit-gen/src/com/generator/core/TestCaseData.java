package com.generator.core;

import java.util.List;

public class TestCaseData {

	private static long nextId = 1L;

	private final long id;
	private final List<Object> paramValues;
	private final ExecutionResult result;

	/**
	 * This method exists because TreeSet doesn't support objects that return
	 * compare == 0 when they are different.<br>
	 * So I had to put an Id on each TestCaseData to differ the objects.
	 */
	private static synchronized long getNextId() {
		return nextId++;
	}

	public TestCaseData(List<Object> paramValues, ExecutionResult result) {
		this.id = getNextId();
		this.paramValues = paramValues;
		this.result = result;
	}

	public List<Object> getParamValues() {
		return paramValues;
	}

	public ExecutionResult getResult() {
		return result;
	}

	public long getId() {
		return id;
	}

}
