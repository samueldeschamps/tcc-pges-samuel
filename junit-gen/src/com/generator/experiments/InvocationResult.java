package com.generator.experiments;

public class InvocationResult {

	public final Object result;
	public final Throwable exception;

	public InvocationResult(Object result) {
		this.result = result;
		this.exception = null;
	}

	public InvocationResult(Throwable exception) {
		this.result = null;
		this.exception = exception;
	}

	public boolean succeeded() {
		return exception == null;
	}

	public boolean failed() {
		return exception != null;
	}

}
