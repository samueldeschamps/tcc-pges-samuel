package com.generator.structure.valuegenerators.random;

import java.util.Random;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;

public class IntegerRandomValues implements ValueGenerator<Integer> {

	private Random random = new Random();
	
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
