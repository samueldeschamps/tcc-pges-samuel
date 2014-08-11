package com.generator.core.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.util.Util;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class CharCommonValues extends CachedValueGenerator<Character> {

	public CharCommonValues() {
		super(initValues());
	}

	private static Character[] initValues() {
		Character[] digits = new CharDigitValues().getValues();
		Character[] upper = new CharUpperLetterValues().getValues();
		Character[] lower = new CharLowerLetterValues().getValues();
		Character[] special = getSpecialChars();
		return Util.arrayConcat(Character.class, digits, upper, lower, special);
	}

	private static Character[] getSpecialChars() {
		return new Character[] { //
		null, ' ', '-', '=', '.', ',', ':', ';', '+', '*', '/', '\\' };
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
