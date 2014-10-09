package com.generator.core.valuegenerators;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class CachedValueGenerator<T> implements ValueGenerator<T> {

	protected final T[] values;
	protected int index;

	public CachedValueGenerator(T[] values) {
		this.values = values;
	}

	@Override
	public boolean hasNext() {
		return index < values.length;
	}

	@Override
	public T next() {
		return values[index++];
	}

	public T[] getValues() {
		return values;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.OTHER;
	}

}
