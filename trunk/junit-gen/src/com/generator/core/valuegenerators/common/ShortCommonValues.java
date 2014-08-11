package com.generator.core.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class ShortCommonValues extends CachedValueGenerator<Short> {

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
