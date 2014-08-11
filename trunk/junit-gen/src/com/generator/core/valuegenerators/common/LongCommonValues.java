package com.generator.core.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class LongCommonValues extends CachedValueGenerator<Long> {

	public LongCommonValues() {
		super(new Long[] { //
				0L, 1L, -1L, 2L, -2L, 3L, -3L, //
						10L, -10L, 100L, -100L, 1000L, -1000L, 10000L, -10000L, //
						(long) Short.MAX_VALUE, (long) Short.MIN_VALUE, //
						(long) Integer.MAX_VALUE, (long) Integer.MIN_VALUE, //
						Long.MAX_VALUE, Long.MIN_VALUE, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
