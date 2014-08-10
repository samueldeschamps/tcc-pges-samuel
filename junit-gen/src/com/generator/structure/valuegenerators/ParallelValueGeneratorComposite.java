package com.generator.structure.valuegenerators;

import java.util.List;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

public class ParallelValueGeneratorComposite<T> implements ValueGenerator<T> {

	private final List<ValueGenerator<T>> generators;
	private int index;

	public ParallelValueGeneratorComposite(List<ValueGenerator<T>> generators) {
		this.generators = generators;
	}

	@Override
	public boolean hasNext() {
		while (!generators.isEmpty()) {
			ValueGenerator<T> gen = generators.get(index);
			if (gen.hasNext()) {
				return true;
			} else {
				generators.remove(index);
			}
		}
		return false;
	}

	@Override
	public T next() {
		T res = generators.get(index).next();
		if (++index >= generators.size()) {
			index = 0;
		}
		return res;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.OTHER;
	}

}
