package com.generator.core.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class CharLowerLetterValues extends CachedValueGenerator<Character> {

	public CharLowerLetterValues() {
		super(initValues());
	}

	private static Character[] initValues() {
		Character[] res = new Character['z' - 'a' + 1];
		int i = 0;
		for (char c = 'a'; c <= 'z'; ++c) {
			res[i++] = c;
		}
		return res;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
