package com.generator.structure;

public class Util {
	
	private static String[] uVowels = new String[] {"A", "E", "I", "O", "U"};
	private static String[] lVowels = new String[] {"a", "e", "i", "o", "u"};

	public static boolean startsWithVowel(String str) {
		for (String vowel : uVowels) {
			if (str.startsWith(vowel)) {
				return true;
			}
		}
		for (String vowel : lVowels) {
			if (str.startsWith(vowel)) {
				return true;
			}
		}
		return false;
	}

}
