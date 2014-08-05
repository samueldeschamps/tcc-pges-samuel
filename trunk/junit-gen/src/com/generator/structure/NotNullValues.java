package com.generator.structure;

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
