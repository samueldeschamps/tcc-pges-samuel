package com.generator.experiments;

import java.lang.reflect.Method;
import java.util.List;

public abstract class ParamValueGenerator {
	
	protected Method method;

	public void setMethod(Method method) {
		this.method = method;
	}
	
	public abstract boolean hasNext();
	
	public abstract List<Object> nextSet();

}
