package com.generator.core;

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
	 * Generate all possible values for that data type (may be infinite for
	 * some).<br>
	 * (Blackbox strategy)
	 */
	BRUTE_FORCE,

	/**
	 * Generate possible values based on how that param is used inside the
	 * method.<br>
	 * (Whitebox strategy)
	 */
	DATA_FLOW_STRATEGY,

	/**
	 * Mixes many strategies in one generator.
	 */
	MIXED,
	
	/**
	 * TODO Rename to a better name.
	 */
	OTHER;

	// TODO Adicionar os demais

}
