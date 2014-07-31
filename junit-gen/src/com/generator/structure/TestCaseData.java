package com.generator.structure;

import java.util.List;

public class TestCaseData {

	private List<Object> paramValues;
	private ExecutionResult result;

	public TestCaseData() {
	}

	public TestCaseData(List<Object> paramValues, ExecutionResult result) {
		this.paramValues = paramValues;
		this.result = result;
	}

	public List<Object> getParamValues() {
		return paramValues;
	}

	public void setParamValues(List<Object> paramValues) {
		this.paramValues = paramValues;
	}

	public ExecutionResult getResult() {
		return result;
	}

	public void setResult(ExecutionResult result) {
		this.result = result;
	}

}
