package com.generator.core;

public enum ExceptionsStrategy {

	/**
	 * Exceptions are always expected in test cases. Put the assertive.
	 */
	ASSERT_ALWAYS,

	/**
	 * Exceptions are expected only when they are declared in method 'throws'
	 * clause. Otherwise, let the test failing.
	 */
	ASSERT_WHEN_DECLARED,
	
	/**
	 * Exceptions are expected only when they are declared in method 'throws'
	 * clause. Otherwise, ignore the situation.
	 */
	IGNORE_WHEN_NOT_DECLARED,

	/**
	 * Do not generate test cases with exception situations.
	 */
	IGNORE_ALWAYS,

}
