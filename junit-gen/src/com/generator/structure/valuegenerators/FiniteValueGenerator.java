package com.generator.structure.valuegenerators;

import com.generator.structure.ValueGenerator;

public abstract class FiniteValueGenerator<T> implements ValueGenerator<T> {
	
	protected final T[] values;
	protected int index;
	
	public FiniteValueGenerator(T[] values) {
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

}
