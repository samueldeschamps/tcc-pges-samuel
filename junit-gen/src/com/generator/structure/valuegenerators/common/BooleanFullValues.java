package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class BooleanFullValues extends CachedValueGenerator<Boolean> {

	public BooleanFullValues() {
		super(new Boolean[] { Boolean.FALSE, Boolean.TRUE, null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

}
