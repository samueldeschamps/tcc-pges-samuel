package com.generator.structure;

public class ExecutionResult {

	private final Object result;
	private final Throwable exception;
	private final boolean exceptionDeclared;
	private final boolean[] coverage;

	public ExecutionResult(Object result) {
		this(result, null);
	}

	public ExecutionResult(Throwable exception, boolean declared) {
		this(exception, declared, null);
	}

	public ExecutionResult(Object result, boolean[] coverage) {
		this.result = result;
		this.exception = null;
		this.exceptionDeclared = false;
		this.coverage = coverage;
	}

	public ExecutionResult(Throwable exception, boolean declared, boolean[] coverage) {
		this.result = null;
		this.exception = exception;
		this.exceptionDeclared = declared;
		this.coverage = coverage;
	}

	public boolean succeeded() {
		return exception == null;
	}

	public boolean failed() {
		return exception != null;
	}
	
	public Object getResult() {
		return result;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public Class<? extends Throwable> getExceptionClass() {
		return exception.getClass();
	}
	
	public boolean isExceptionDeclared() {
		return exceptionDeclared;
	}
	
	public boolean[] getCoverage() {
		return coverage;
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
