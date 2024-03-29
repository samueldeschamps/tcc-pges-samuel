package com.generator.core.valuegenerators.random;

import java.util.Random;

import com.generator.core.RandomProvider;
import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public abstract class BaseStringRandomValues implements ValueGenerator<String> {

	private final char[] alphabet;
	private final int maxLength;

	public BaseStringRandomValues(char[] alphabet, int maxLength) {
		this.alphabet = alphabet;
		this.maxLength = maxLength;
	}

	private Random random = RandomProvider.createRandom();

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public String next() {
		int length = random.nextInt(maxLength);
		return nextString(length);
	}

	private String nextString(int length) {
		char[] res = new char[length];
		for (int i = 0; i < length; ++i) {
			res[i] = alphabet[random.nextInt(alphabet.length)];
		}
		return String.valueOf(res);
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
