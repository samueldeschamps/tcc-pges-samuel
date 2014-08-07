package com.generator.structure.valuegenerators;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;

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
