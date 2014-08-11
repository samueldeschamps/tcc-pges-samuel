package com.generator.core.valuegenerators.random;

public class StringNumericRandomValues extends BaseStringRandomValues {

	private static final char[] ALPHABET = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static final int MAX_LENGTH = 20;

	public StringNumericRandomValues() {
		super(ALPHABET, MAX_LENGTH);
	}

}
