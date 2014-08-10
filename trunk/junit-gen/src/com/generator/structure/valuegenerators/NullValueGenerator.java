package com.generator.structure.valuegenerators;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;

public class NullValueGenerator<T> implements ValueGenerator<T> {

	private boolean done = false;

	@Override
	public boolean hasNext() {
		return !done;
	}

	@Override
	public T next() {
		done = true;
		return null;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
