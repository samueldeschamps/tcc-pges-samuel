package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.FiniteValueGenerator;

public class ShortCommonValues extends FiniteValueGenerator<Short> {

	public ShortCommonValues() {
		super(new Short[] { //
				0, 1, -1, 2, -2, 3, -3, //
						10, -10, 100, -100, 1000, -1000, 10000, -10000, //
						Short.MIN_VALUE, Short.MAX_VALUE, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
