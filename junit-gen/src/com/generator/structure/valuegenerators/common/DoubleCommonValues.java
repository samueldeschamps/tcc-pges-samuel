package com.generator.structure.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class DoubleCommonValues extends CachedValueGenerator<Double> {

	public DoubleCommonValues() {
		super(new Double[] { //
				0.0, 1.0, -1.0, 2.0, -2.0, 2.5, -2.5, 3.0, -3.0, //
						10.0, -10.0, 1000.0, -1000.0, 1234.56, -1234.56, //
						1000000.0, -1000000.0, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
