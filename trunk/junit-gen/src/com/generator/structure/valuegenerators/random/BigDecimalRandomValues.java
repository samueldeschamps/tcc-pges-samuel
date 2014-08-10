package com.generator.structure.valuegenerators.random;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.generator.structure.ValueGenerationStrategy;
import com.generator.structure.ValueGenerator;

public class BigDecimalRandomValues implements ValueGenerator<BigDecimal> {

	private static final int MAX_SCALE = 10;
	private DoubleRandomValues inner = new DoubleRandomValues();

	@Override
	public boolean hasNext() {
		return inner.hasNext();
	}

	@Override
	public BigDecimal next() {
		return doubleToBigDecimal(inner.next());
	}

	private BigDecimal doubleToBigDecimal(Double value) {
		if (value == null) {
			return null;
		}
		BigDecimal res = new BigDecimal(value).setScale(MAX_SCALE, RoundingMode.HALF_UP);
		return removeTrailingZeros(res);
	}

	private BigDecimal removeTrailingZeros(BigDecimal res) {
		if (res.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return res.stripTrailingZeros();
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.RANDOM;
	}

}
