package com.generator.core.valuegenerators.common;

import java.math.BigDecimal;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class BigDecimalCommonValues extends CachedValueGenerator<BigDecimal> {

	public BigDecimalCommonValues() {
		super(new BigDecimal[] { //
				new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("-1"), //
						new BigDecimal("2"), new BigDecimal("-2"), new BigDecimal("2.5"), //
						new BigDecimal("-2.5"), new BigDecimal("3"), new BigDecimal("-3"), //
						new BigDecimal("10"), new BigDecimal("-10"), new BigDecimal("1000"), //
						new BigDecimal("-1000"), new BigDecimal("1234.56"), new BigDecimal("-1234.56"), //
						new BigDecimal("1000000"), new BigDecimal("-1000000"), null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
