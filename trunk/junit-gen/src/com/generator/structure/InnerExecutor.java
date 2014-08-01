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

		} catch (InvocationTargetException ex) {

			boolean declared = false;
			if (ex.getCause() != null) {
				for (Class<?> throwEx : method.getExceptionTypes()) {
					if (throwEx.isAssignableFrom(ex.getCause().getClass())) {
						declared = true;
					}
				}
			}
			return new ExecutionResult(ex.getCause(), declared);

		} catch (IllegalAccessException | IllegalArgumentException e) {

			throw new RuntimeException("Error invoking method '" + method.getName() + "'.", e);
		}
	}

}
