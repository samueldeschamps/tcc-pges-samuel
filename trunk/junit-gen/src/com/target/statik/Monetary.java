package com.target.statik;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class Monetary {

	private static final int DECIMAL_DIGITS = 2;
	private static final BigDecimal ONE_CENT = BigDecimal.ONE.movePointLeft(DECIMAL_DIGITS);

	public static BigDecimal[] installments(BigDecimal totalValue, int installments) {
		return installments(totalValue, installments, RemainderStrategy.FIRST_N);
	}

	public static BigDecimal[] installments(BigDecimal totalValue, int installments, RemainderStrategy strategy) {
		
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
		switch (strategy) {
		case FIRST_1:
			result[0] = result[0].add(remainder);
			break;
		case LAST_1:
			result[result.length - 1] = result[result.length - 1].add(remainder);
			break;
		case FIRST_N:
			for (int i = 0; remainder.compareTo(BigDecimal.ZERO) > 0; ++i) {
				result[i] = result[i].add(ONE_CENT);
				remainder = remainder.subtract(ONE_CENT);
			}
			break;
		case LAST_N:
			for (int i = result.length - 1; remainder.compareTo(BigDecimal.ZERO) > 0; --i) {
				result[i] = result[i].add(ONE_CENT);
				remainder = remainder.subtract(ONE_CENT);
			}
			break;
		}
		return result;
	}

	public enum RemainderStrategy {
		FIRST_1, FIRST_N, LAST_1, LAST_N
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(installments(new BigDecimal("101"), 7, RemainderStrategy.FIRST_N)));
	}

}
