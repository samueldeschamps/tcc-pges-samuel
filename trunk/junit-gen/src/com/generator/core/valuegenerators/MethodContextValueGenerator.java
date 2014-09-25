package com.generator.core.valuegenerators;

import java.lang.reflect.Method;

import com.generator.core.Context;
import com.generator.core.ValueGenerator;
import com.generator.core.util.Log;

public abstract class MethodContextValueGenerator<T> implements ValueGenerator<T> {

	private final Method method;
	private final int paramIdx;
	private final String paramName;

	protected MethodContextValueGenerator() {
		this.method = Context.get().getCurrMethod();
		this.paramIdx = Context.get().getCurrParamIdx();
		this.paramName = findParamName();
	}

	private String findParamName() {
		try {
			return Context.get().getCodeInfo().getParamNames(method).get(paramIdx);
		} catch (Throwable e) {
			Log.warning("Could not find param name for '" + method.getName() + "', param " + paramIdx + ".");
			return null;
		}
	}

	public Method getMethod() {
		return method;
	}

	public int getParamIdx() {
		return paramIdx;
	}

	public String getParamName() {
		return paramName;
	}

}
