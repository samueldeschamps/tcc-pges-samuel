package com.generator.structure.valuegenerators.random;

import java.util.Random;

import com.generator.structure.RandomProvider;
import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;

public class StringNumericRandomValues implements ValueGenerator<String> {

	private static final int MAX_LENGTH = 20;
	private static final char[] NUMBERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private Random random = RandomProvider.createRandom();

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public String next() {
		int length = random.nextInt(MAX_LENGTH);
		return nextString(length);
	}

	private String nextString(int length) {
		char[] res = new char[length];
		for (int i = 0; i < length; ++i) {
			res[i] = NUMBERS[random.nextInt(10)];
		}
		return String.valueOf(res);
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
