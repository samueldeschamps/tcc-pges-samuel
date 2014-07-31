package com.generator.structure;

import java.util.List;

public class ValueGeneratorComposite<T> implements ValueGenerator<T> {

	private List<ValueGenerator<T>> generators;
	private int index;
	private ValueGenerator<T> current;

	public ValueGeneratorComposite(List<ValueGenerator<T>> generators) {
		this.generators = generators;
	}

	@Override
	public boolean hasNext() {
		if (!prepare()) {
			return false;
		}
		if (current.hasNext()) {
			return true;
		}
		for (int i = index + 1; i < generators.size(); ++i) {
			if (generators.get(i).hasNext()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public T next() {
		if (current.hasNext()) {
			return current.next();
		}
		for (int i = index + 1; i < generators.size(); ++i) {
			if (generators.get(i).hasNext()) {
				current = generators.get(i);
				index = i;
				break;
			}
		}
		return current.next();
	}

	private boolean prepare() {
		if (current == null) {
			if (generators.size() == 0) {
				return false;
			}
			current = generators.get(index);
		}
		return true;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.MIXED;
	}

}
