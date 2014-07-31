package com.generator.structure;

public class ExecutionResult {

	public final Object result;
	public final Throwable exception;
	public final boolean[] coverage;

	public ExecutionResult(Object result) {
		this(result, null);
	}

	public ExecutionResult(Throwable exception) {
		this(exception, null);
	}

	public ExecutionResult(Object result, boolean[] coverage) {
		this.result = result;
		this.exception = null;
		this.coverage = coverage;
	}

	public ExecutionResult(Throwable exception, boolean[] coverage) {
		this.result = null;
		this.exception = exception;
		this.coverage = coverage;
	}

	public boolean succeeded() {
		return exception == null;
	}

	public boolean failed() {
		return exception != null;
	}

	public Class<? extends Throwable> getExceptionClass() {
		return exception.getClass();
	}

	public double getCoverageRate() {
		if (coverage.length == 0) {
			return 0.0;
		}
		int trueCount = 0;
		for (boolean b : coverage) {
			if (b) {
				++trueCount;
			}
		}
		return ((double) trueCount) / coverage.length;
	}

}
