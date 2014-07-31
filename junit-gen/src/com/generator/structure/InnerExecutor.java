package com.generator.structure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class InnerExecutor {

	private final Method method;

	public InnerExecutor(Method method) {
		this.method = method;
	}

	public ExecutionResult execute(List<Object> params) {

		// TODO Collect the execution Coverage.
		try {
			Object result = method.invoke(null, params.toArray());
			return new ExecutionResult(result);

		} catch (InvocationTargetException e) {
			return new ExecutionResult(e.getCause());

		} catch (IllegalAccessException | IllegalArgumentException e) {

			throw new RuntimeException("Error invoking method.", e);
		}
	}

}
