package com.generator.core.valuegenerators.full;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class CharFullValues implements ValueGenerator<Character> {

	private static final int LIMIT = 1 << Character.SIZE;

	private int index = 0;
	private char current = Character.MIN_VALUE;

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

	@Override
	public boolean hasNext() {
		return index <= LIMIT;
	}

	@Override
	public Character next() {
		if (index++ == LIMIT) {
			return null;
		} else {
			return current++;
		}
	}

}
