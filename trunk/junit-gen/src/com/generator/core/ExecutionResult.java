package com.generator.core;

public class ExecutionResult {

	private final Object result;
	private final Throwable exception;
	private final boolean exceptionDeclared;
	private final boolean timedOut;
	private final CoverageInfo coverageInfo;

	public ExecutionResult(Object result) {
		this(result, null);
	}

	public ExecutionResult(boolean timedOut) {
		this(null, false, null, true);
	}

	public ExecutionResult(Throwable exception, boolean declared) {
		this(exception, declared, null, false);
	}

	public ExecutionResult(Object result, CoverageInfo coverageInfo) {
		this.result = result;
		this.exception = null;
		this.exceptionDeclared = false;
		this.timedOut = false;
		this.coverageInfo = coverageInfo;
	}

	public ExecutionResult(Throwable exception, boolean declared, CoverageInfo coverageInfo, boolean timedOut) {
		this.result = null;
		this.exception = exception;
		this.exceptionDeclared = declared;
		this.timedOut = timedOut;
		this.coverageInfo = coverageInfo;
	}

	public ExecutionResult(ExecutionResult other, CoverageInfo coverageInfo) {
		this.result = other.result;
		this.exception = other.exception;
		this.exceptionDeclared = other.exceptionDeclared;
		this.timedOut = other.timedOut;
		this.coverageInfo = coverageInfo;
	}

	public boolean succeeded() {
		return exception == null && !timedOut;
	}

	public boolean failed() {
		return exception != null && !timedOut;
	}

	public boolean isInfiniteLoop() {
		return timedOut;
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

	public double getShallowCoverageRatio() {
		return coverageInfo.getShallowCounter().getRatio();
	}

}
