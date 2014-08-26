package com.generator.core.res.input;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Monetary {

	private static final int MAX_INSTALLMENTS = 360;
	private static final int DECIMAL_DIGITS = 2;
	private static final BigDecimal ONE_CENT = BigDecimal.ONE.movePointLeft(DECIMAL_DIGITS);

	public static BigDecimal[] installments(BigDecimal totalValue, int installments) throws IllegalArgumentException {
		return installments(totalValue, installments, RemainderStrategy.FIRST_N);
	}

	public static BigDecimal[] installments(BigDecimal totalValue, int installments, RemainderStrategy strategy)
			throws IllegalArgumentException {
		if (installments > MAX_INSTALLMENTS) {
			throw new IllegalArgumentException("Installments cannot be greater than " + MAX_INSTALLMENTS + "!");
		}
		if (installments == 0) {
			return new BigDecimal[0];
		}

		BigDecimal qtd = new BigDecimal(installments);
		BigDecimal quotient = totalValue.divide(qtd, DECIMAL_DIGITS, RoundingMode.FLOOR);

		BigDecimal[] result = new BigDecimal[installments];
		for (int i = 0; i < result.length; ++i) {
			result[i] = quotient;
		}

		BigDecimal remainder = totalValue.subtract(quotient.multiply(qtd));
		if (strategy == RemainderStrategy.FIRST_1) {
			result[0] = result[0].add(remainder);
		} else if (strategy == RemainderStrategy.LAST_1) {
			result[result.length - 1] = result[result.length - 1].add(remainder);
		} else if (strategy == RemainderStrategy.FIRST_N) {
			for (int i = 0; remainder.compareTo(BigDecimal.ZERO) > 0; ++i) {
				result[i] = result[i].add(ONE_CENT);
				remainder = remainder.subtract(ONE_CENT);
			}
		} else if (strategy == RemainderStrategy.LAST_N) {
			for (int i = result.length - 1; remainder.compareTo(BigDecimal.ZERO) > 0; --i) {
				result[i] = result[i].add(ONE_CENT);
				remainder = remainder.subtract(ONE_CENT);
			}
		}
		return result;
	}

	public enum RemainderStrategy {
		FIRST_1, FIRST_N, LAST_1, LAST_N
	}

}
