package com.generator.experiments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class DynamicExecutor {

	private String className;
	private String methodName; // TODO Use a 'Method' instead?

	public void setClassName(String className) {
		this.className = className;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public InvocationResult getResultFromParams(List<Object> params) throws ReflectiveOperationException {

		Class<?> clazz = Class.forName(className);
		Method method = clazz.getMethod(methodName, int.class, int.class); // TODO

		try {
			Object result = method.invoke(null, params.toArray());
			return new InvocationResult(result);
			
		} catch (InvocationTargetException e) {
			return new InvocationResult(e.getCause());
		}
	}

}
