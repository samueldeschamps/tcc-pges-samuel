package com.generator.core.valuegenerators.random;

import com.generator.core.util.Util;

public class StringAlphabeticCapitalizedRandomValues extends BaseStringRandomValues {

	private static final char[] ALPHABET = new char[] { //
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', //
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private static final int MAX_LENGTH = 20;

	public StringAlphabeticCapitalizedRandomValues() {
		super(ALPHABET, MAX_LENGTH);
	}
	
	@Override
	public String next() {
		String res = super.next();
		return Util.capitalize(res);
	};

}
