package com.generator.structure.valuegenerators.full;

import com.generator.core.ValueGenerationStrategy;
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
