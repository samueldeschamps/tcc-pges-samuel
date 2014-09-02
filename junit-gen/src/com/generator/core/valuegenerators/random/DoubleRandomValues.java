package com.generator.core.valuegenerators.random;

import java.text.DecimalFormat;
import java.util.Random;

import com.generator.core.RandomProvider;
import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;
import com.generator.core.util.Util;

// TODO This guy could generate better distributed values
public class DoubleRandomValues implements ValueGenerator<Double> {

	private static final int MAX_MULTIPLIER = 10000;
	private static final int MAX_SCALE = 5;
	private static final int MIN_SCALE = -5;

	private Random drandom = RandomProvider.createRandom();
	private Random irandom = RandomProvider.createRandom();

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Double next() {
		double value = drandom.nextDouble();
		double multiplier = irandom.nextInt(MAX_MULTIPLIER);
		int scale = irandom.nextInt(MAX_SCALE - MIN_SCALE) + MIN_SCALE;
		return Util.round(value * multiplier, scale);
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
