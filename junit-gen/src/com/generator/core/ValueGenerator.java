package com.generator.core;

public interface ValueGenerator<T> {

	public abstract boolean hasNext();

	public abstract T next();

	public abstract ValueGenerationStrategy getStrategy();

}
