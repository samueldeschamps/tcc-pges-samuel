package com.generator.core;


public class ExecutionResult {

	private final Object result;
	private final Throwable exception;
	private final boolean exceptionDeclared;
	private final CoverageInfo coverageInfo;

	public ExecutionResult(Object result) {
		this(result, null);
	}

	public ExecutionResult(Throwable exception, boolean declared) {
		this(exception, declared, null);
	}

	public ExecutionResult(Object result, CoverageInfo coverageInfo) {
		this.result = result;
		this.exception = null;
		this.exceptionDeclared = false;
		this.coverageInfo = coverageInfo;
	}

	public ExecutionResult(Throwable exception, boolean declared, CoverageInfo coverageInfo) {
		this.result = null;
		this.exception = exception;
		this.exceptionDeclared = declared;
		this.coverageInfo = coverageInfo;
	}

	public ExecutionResult(ExecutionResult other, CoverageInfo coverageInfo) {
		this.result = other.result;
		this.exception = other.exception;
		this.exceptionDeclared = other.exceptionDeclared;
		this.coverageInfo = coverageInfo;
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

	public CoverageInfo getCoverageInfo() {
		return coverageInfo;
	}

	public boolean hasCoverageInfo() {
		return coverageInfo != null;
	}

	public double getCoverageRatio() {
		return coverageInfo.getCoverageRatio();
	}

}
