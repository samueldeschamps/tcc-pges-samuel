package com.generator.core;

public interface ValueGenerator<T> {
	
	public boolean hasNext();
	
	public T next();
	
	public ValueGenerationStrategy getStrategy();
	
}
