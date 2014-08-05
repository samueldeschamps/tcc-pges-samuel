package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.FiniteValueGenerator;

public class StringCommonValues extends FiniteValueGenerator<String> {

	public StringCommonValues() {
		super(new String[] { //
				"", "a", "A", "abc", "ABC", "123", " ", "abc123", null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
