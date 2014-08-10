package com.generator.structure.valuegenerators.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.generator.structure.RandomProvider;
import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;
import com.generator.structure.ValueGeneratorRegistry;
import com.generator.structure.valuegenerators.EmptyArrayValueGenerator;
import com.generator.structure.valuegenerators.FixedArrayValuesGenerator;
import com.generator.structure.valuegenerators.NullValueGenerator;

public class RandomArrayValuesGenerator<T> implements ValueGenerator<T[]> {

	private static final int MAX_LENGTH = 20;

	private final List<ValueGenerator<T>> inners = new ArrayList<>(MAX_LENGTH);
	private final Random random = RandomProvider.createRandom();
	private T[] next;

	@SuppressWarnings("unchecked")
	public RandomArrayValuesGenerator(ValueGeneratorRegistry registry, Class<T> arrayType) {
		
		// We also need to generate the "null" value, and the empty array:
		inners.add(new NullValueGenerator<T>());
		inners.add((ValueGenerator<T>) new EmptyArrayValueGenerator<T>(arrayType));
		
		for (int i = 1; i < MAX_LENGTH; ++i) {
			FixedArrayValuesGenerator<T> gen = new FixedArrayValuesGenerator<T>(registry, arrayType, i);
			inners.add((ValueGenerator<T>) gen);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasNext() {
		while (!inners.isEmpty()) {
			int genIdx = random.nextInt(inners.size());
			ValueGenerator<T> gen = inners.get(genIdx);
			if (gen.hasNext()) {
				next = (T[]) gen.next();
				return true;
			} else {
				inners.remove(genIdx);
			}
		}
		return false;
	}

	@Override
	public T[] next() {
		return next;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
