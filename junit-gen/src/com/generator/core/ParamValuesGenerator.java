package com.generator.core;

import java.util.List;

public class ParamValuesGenerator {

	private ValueSetGenerator inner;

	public ParamValuesGenerator(ValueGeneratorRegistry registry, Class<?>[] paramTypes) {
		ValueGenerator<?>[] generators = new ValueGenerator<?>[paramTypes.length];
		for (int i = 0; i < paramTypes.length; ++i) {
			generators[i] = registry.getComposite(paramTypes[i]);
		}
		inner = new ValueSetGenerator(generators);
	}

	public boolean hasNext() {
		return inner.hasNext();
	}

	public List<Object> next() {
		return inner.next();
	}

}
