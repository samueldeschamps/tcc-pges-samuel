package com.generator.core.valuegenerators;

import java.lang.reflect.Method;

import com.generator.core.Context;
import com.generator.core.ValueGenerator;

public abstract class MethodContextValueGenerator<T> implements ValueGenerator<T> {

	private Method method;
	private int paramIdx;

	protected MethodContextValueGenerator() {
		this.method = Context.get().getCurrMethod();
		this.paramIdx = Context.get().getCurrParamIdx();
	}

	public Method getMethod() {
		return method;
	}

	public int getParamIdx() {
		return paramIdx;
	}

}
