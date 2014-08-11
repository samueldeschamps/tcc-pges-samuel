package com.generator.core.valuegenerators;

import com.generator.core.ValueGenerator;

public abstract class FilteredValues<T> implements ValueGenerator<T> {

	private final ValueGenerator<T> source;
	private T next;

	public FilteredValues(ValueGenerator<T> source) {
		this.source = source;
	}

	public boolean hasNext() {
		for (;;) {
			if (!source.hasNext()) {
				next = null;
				return false;
			} else {
				next = source.next();
				if (filter(next)) {
					return true;
				}
			}
		}
	}

	// TODO Support calling next twice.
	public T next() {
		return next;
	}
	
	protected abstract boolean filter(T value);

}
