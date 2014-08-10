package com.generator.structure.valuegenerators.full;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class ByteFullValues implements ValueGenerator<Byte> {

	private static final int LIMIT = 1 << Byte.SIZE;

	private int index = 0;
	private byte current = Byte.MIN_VALUE;

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

	@Override
	public boolean hasNext() {
		return index <= LIMIT;
	}

	@Override
	public Byte next() {
		if (index++ == LIMIT) {
			return null;
		} else {
			return current++;
		}
	}

}
