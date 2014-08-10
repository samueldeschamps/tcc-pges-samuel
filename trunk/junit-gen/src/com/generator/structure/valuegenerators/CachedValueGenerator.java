package com.generator.structure.valuegenerators;

import com.generator.core.ValueGenerator;

public abstract class CachedValueGenerator<T> implements ValueGenerator<T> {
	
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

}
