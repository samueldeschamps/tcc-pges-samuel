package com.generator.structure.valuegenerators;

import java.util.LinkedList;
import java.util.List;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;

/**
 * Reads all values from one ValueGenerator, then goes to the next and so on.
 */
public class SerialValueGeneratorComposite<T> implements ValueGenerator<T> {

	private LinkedList<ValueGenerator<T>> generators;

	public SerialValueGeneratorComposite(List<ValueGenerator<T>> generators) {
		this.generators = new LinkedList<>(generators);
	}

	@Override
	public boolean hasNext() {
		while (!generators.isEmpty()) {
			ValueGenerator<T> gen = generators.getFirst();
			if (gen.hasNext()) {
				return true;
			} else {
				generators.removeFirst();
			}
		}
		return false;
	}

	@Override
	public T next() {
		return generators.getFirst().next();
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.OTHER;
	}

}
