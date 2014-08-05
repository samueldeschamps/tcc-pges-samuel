package com.generator.structure;

import java.util.LinkedHashSet;
import java.util.Set;

public class UniqueValues<T> extends FilteredValues<T> {

	private final Set<T> history = new LinkedHashSet<>();

	public UniqueValues(ValueGenerator<T> source) {
		super(source);
	}
	
	@Override
	public T next() {
		T res = super.next();
		history.add(res);
		return res;
	}

	@Override
	protected boolean filter(T value) {
		return !history.contains(value);
	}

	public Set<T> getHistory() {
		return history;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.OTHER;
	}

}
