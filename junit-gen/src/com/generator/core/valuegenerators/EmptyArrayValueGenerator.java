package com.generator.core.valuegenerators;

import java.lang.reflect.Array;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;
import com.generator.core.util.Util;

public class EmptyArrayValueGenerator<T> implements ValueGenerator<T[]> {

	private final Class<?> componentType;
	private boolean done = false;

	public EmptyArrayValueGenerator(Class<T> arrayType) {
		Class<?> compType = arrayType.getComponentType();
		if (compType.isPrimitive()) {
			compType = Util.primitiveToWrapper(compType);
		}
		this.componentType = compType;
	}

	@Override
	public boolean hasNext() {
		return !done;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] next() {
		done = true;
		return (T[]) Array.newInstance(componentType, 0);
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
