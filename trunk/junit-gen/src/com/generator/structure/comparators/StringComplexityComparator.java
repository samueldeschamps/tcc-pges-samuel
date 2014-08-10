package com.generator.structure.comparators;

import java.util.Comparator;

public class StringComplexityComparator implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		// Digits before letters:
		int res = Boolean.compare(!hasOnlyDigits(s1), !hasOnlyDigits(s2));
		if (res != 0) {
			return res;
		}
		// If same, digits/letters before special characters:
		res = Boolean.compare(hasSpecialChars(s1), hasSpecialChars(s2));
		if (res != 0) {
			return res;
		}
		// If same, shorter strings before longer strings:
		res = Integer.compare(s1.length(), s2.length());
		if (res != 0) {
			return res;
		}
		// If same, use the default string comparator:
		return s1.compareTo(s2);
	}
	
	private boolean hasOnlyDigits(String str) {
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private boolean hasSpecialChars(String str) {
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (!Character.isDigit(c) && !Character.isLetter(c)) {
				return true;
			}
		}
		return false;
	}

}
