package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class IntegerCommonValues extends CachedValueGenerator<Integer> {

	public IntegerCommonValues() {
		super(new Integer[] { //
				0, 1, -1, 2, -2, 3, -3, //
						10, -10, 100, -100, 1000, -1000, 10000, -10000, //
						(int) Short.MAX_VALUE, (int) Short.MIN_VALUE, //
						Integer.MAX_VALUE, Integer.MIN_VALUE, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
