package com.generator.structure.valuegenerators.random;

public class StringAlphabeticUpperRandomValues extends BaseStringRandomValues {

	private static final char[] ALPHABET = new char[] { //
	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', //
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static final int MAX_LENGTH = 20;

	public StringAlphabeticUpperRandomValues() {
		super(ALPHABET, MAX_LENGTH);
	}

}
