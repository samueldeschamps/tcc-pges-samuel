package com.generator.core.valuegenerators.random;

import java.math.BigInteger;
import java.util.Random;

import com.generator.core.RandomProvider;
import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class BigIntegerRandomValues implements ValueGenerator<BigInteger> {

	private Random random = RandomProvider.createRandom();

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public BigInteger next() {
		return BigInteger.valueOf(random.nextLong());
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
