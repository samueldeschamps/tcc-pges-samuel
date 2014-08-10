package com.generator.structure.valuegenerators.random;

import java.util.Random;

import com.generator.core.RandomProvider;
import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class IntegerRandomValues implements ValueGenerator<Integer> {

	private Random random = RandomProvider.createRandom();
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Integer next() {
		return random.nextInt();
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
