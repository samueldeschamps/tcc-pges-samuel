package com.generator.structure;

public enum ValueGenerationStrategy {
	
	/**
	 * Generate the most common known values for that data type.<br>
	 * (Blackbox strategy)
	 */
	COMMON_VALUES, 
	
	/**
	 * Generate random values for that data type.<br>
	 * (Blackbox strategy)
	 */
	RANDOM, 
	
	/**
	 * Generate all possible values for that data type (may be infinite for some).<br>
	 * (Blackbox strategy)
	 */
	BRUTE_FORCE,
	
	/**
	 * Generate possible values based on how that param is used inside the method.<br>
	 * (Whitebox strategy)
	 */
	DATA_FLOW_STRATEGY,
	
	/**
	 * Mixes many strategies in one generator.
	 */
	MIXED,
	
	// TODO Adicionar os demais
	
	// TODO Vai poder haver mais de um gerador por tipo?
	
}
