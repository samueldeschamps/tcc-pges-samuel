package com.generator.core.valuegenerators.full;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class BooleanFullValues extends CachedValueGenerator<Boolean> {

	public BooleanFullValues() {
		super(new Boolean[] { Boolean.FALSE, Boolean.TRUE, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

}
