package com.generator.structure.valuegenerators.full;

import com.generator.core.ValueGenerationStrategy;
import com.generator.structure.valuegenerators.CachedValueGenerator;

@SuppressWarnings("rawtypes")
public class EnumFullValues extends CachedValueGenerator<Enum> {

	public EnumFullValues(Class<? extends Enum<?>> enumClass) {
		super(initValues(enumClass));
	}

	private static Enum<?>[] initValues(Class<? extends Enum<?>> enumClass) {
		Enum<?>[] consts = enumClass.getEnumConstants();
		Enum<?>[] result = new Enum<?>[consts.length + 1];
		System.arraycopy(consts, 0, result, 0, consts.length);
		result[consts.length] = null;
		return result;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

}
