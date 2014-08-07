package com.generator.structure.valuegenerators.random;

public class StringAlphabeticLowerRandomValues extends BaseStringRandomValues {

	private static final char[] ALPHABET = new char[] { //
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', //
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private static final int MAX_LENGTH = 20;

	public StringAlphabeticLowerRandomValues() {
		super(ALPHABET, MAX_LENGTH);
	}

}
