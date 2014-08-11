package com.generator.core.valuegenerators;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

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
