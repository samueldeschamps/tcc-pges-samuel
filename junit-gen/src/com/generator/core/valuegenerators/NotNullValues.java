package com.generator.core.valuegenerators;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class NotNullValues<T> extends FilteredValues<T> {

	public NotNullValues(ValueGenerator<T> source) {
		super(source);
	}

	@Override
	protected boolean filter(T value) {
		return value != null;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.OTHER;
	}
	
}
