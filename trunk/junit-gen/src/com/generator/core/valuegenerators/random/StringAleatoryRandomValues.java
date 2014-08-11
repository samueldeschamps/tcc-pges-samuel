package com.generator.core.valuegenerators.random;

public class StringAleatoryRandomValues extends BaseStringRandomValues {

	// TODO Use a well formed charset instead:
	private static final char[] ALPHABET = new char[] { //
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', //
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', //
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', //
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', //
			' ', ',', '.', ':', ';', '-', '=', '*', '/', '+', '_', '\\', '!', //
			'?', '(', ')', '&', '|', '"', '\'', '#', '@', '%', '<', '>', '{', //
			'}', '[', ']', 'ç', 'Ç', 'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', //
			'Ó', 'Ú', 'â', 'Â', 'ê', 'Ê', 'ô', 'Ô', 'ã', 'Ã', 'õ', 'Õ' };

	private static final int MAX_LENGTH = 20;

	public StringAleatoryRandomValues() {
		super(ALPHABET, MAX_LENGTH);
	}

}
