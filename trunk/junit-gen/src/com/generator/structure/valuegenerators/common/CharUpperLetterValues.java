package com.generator.structure.valuegenerators.common;

import java.util.Arrays;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

public class CharUpperLetterValues extends CachedValueGenerator<Character> {

	public CharUpperLetterValues() {
		super(initValues());
	}

	private static Character[] initValues() {
		Character[] res = new Character['Z' - 'A' + 1];
		int i = 0;
		for (char c = 'A'; c <= 'Z'; ++c) {
			res[i++] = c;
		}
		return res;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(initValues()));
	}

}
