package com.generator.core.valuegenerators.common;

import java.math.BigInteger;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;

public class BigIntegerCommonValues extends CachedValueGenerator<BigInteger> {

	public BigIntegerCommonValues() {
		super(new BigInteger[] { //
				BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(-1), //
						BigInteger.valueOf(2), BigInteger.valueOf(-2), BigInteger.valueOf(3), //
						BigInteger.valueOf(-3), BigInteger.valueOf(10), BigInteger.valueOf(-10), //
						BigInteger.valueOf(100), BigInteger.valueOf(-100), BigInteger.valueOf(1000), //
						BigInteger.valueOf(-1000), BigInteger.valueOf(10000), BigInteger.valueOf(-10000), //
						BigInteger.valueOf(Short.MAX_VALUE), BigInteger.valueOf(Short.MIN_VALUE), //
						BigInteger.valueOf(Integer.MAX_VALUE), BigInteger.valueOf(Integer.MIN_VALUE), //
						BigInteger.valueOf(Long.MAX_VALUE), BigInteger.valueOf(Long.MIN_VALUE), //
						BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE), //
						BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE), //
						null });
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.COMMON_VALUES;
	}

}
