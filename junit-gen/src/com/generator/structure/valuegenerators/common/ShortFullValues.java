package com.generator.structure.valuegenerators.common;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;

public class ShortFullValues implements ValueGenerator<Short> {

	private static final int LIMIT = 1 << Short.SIZE;

	private int index = 0;
	private short current = Short.MIN_VALUE;

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

	@Override
	public boolean hasNext() {
		return index <= LIMIT;
	}

	@Override
	public Short next() {
		if (index++ == LIMIT) {
			return null;
		} else {
			return current++;
		}
	}
	
}
