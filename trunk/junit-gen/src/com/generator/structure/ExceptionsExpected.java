package com.generator.structure;

public enum ExceptionsExpected {

	/**
	 * Exceptions are always expected in test cases.
	 */
	YES,
	
	/**
	 * Exceptions are expected only when they are declared in method 'throws' clause. 
	 */
	WHEN_NOT_DECLARED,

}
