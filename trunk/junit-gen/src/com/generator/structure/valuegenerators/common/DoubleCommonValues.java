package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.FiniteValueGenerator;

public class DoubleCommonValues extends FiniteValueGenerator<Double> {

	public DoubleCommonValues() {
		super(new Double[] { //
				0.0, 1.0, -1.0, 2.5, -2.5, 3.0, -3.0, 10.0, -10.0, 1000.0, -1000.0, 1234.56, -1234.56 });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
