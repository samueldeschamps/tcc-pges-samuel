package com.generator.core.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class CharDigitValues extends CachedValueGenerator<Character> {

	public CharDigitValues() {
		super(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
