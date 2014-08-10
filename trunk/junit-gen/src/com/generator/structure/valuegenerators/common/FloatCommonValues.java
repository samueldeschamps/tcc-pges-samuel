package com.generator.structure.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class FloatCommonValues extends CachedValueGenerator<Float> {

	public FloatCommonValues() {
		super(new Float[] { //
				(float) 0.0, (float) 1.0, (float) -1.0, (float) 2.0, (float) -2.0, //
						(float) 2.5, (float) -2.5, (float) 3.0, (float) -3.0, //
						(float) 10.0, (float) -10.0, (float) 1000.0, (float) -1000.0, //
						(float) 1234.56, (float) -1234.56, (float) 1000000.0, (float) -1000000.0, null });

	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
