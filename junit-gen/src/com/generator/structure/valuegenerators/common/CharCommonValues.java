package com.generator.structure.valuegenerators.common;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.util.Util;
import com.generator.structure.valuegenerators.CachedValueGenerator;

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
