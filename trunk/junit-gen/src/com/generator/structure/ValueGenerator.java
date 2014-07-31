package com.generator.structure;

public interface ValueGenerator<T> {
	
	public boolean hasNext();
	
	public T next();
	
	public ValueGenerationStrategy getStrategy();
	
}
