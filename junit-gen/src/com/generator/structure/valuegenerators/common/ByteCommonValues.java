package com.generator.structure.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class ByteCommonValues extends CachedValueGenerator<Byte> {

	public ByteCommonValues() {
		super(new Byte[] { //
				0, 1, -1, 2, -2, 3, -3, //
						10, -10, 100, -100, //
						Byte.MAX_VALUE, Byte.MIN_VALUE, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
