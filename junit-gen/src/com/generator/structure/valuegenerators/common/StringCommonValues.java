package com.generator.structure.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class StringCommonValues extends CachedValueGenerator<String> {

	public StringCommonValues() {
		super(new String[] { //
				"", "a", "A", "abc", "ABC", "123", " ", "abc123", null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
