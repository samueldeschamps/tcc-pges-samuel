package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.FiniteValueGenerator;

public class CharDigitValues extends FiniteValueGenerator<Character> {

	public CharDigitValues() {
		super(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
