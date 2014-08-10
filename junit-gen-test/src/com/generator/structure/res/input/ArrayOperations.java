package com.generator.structure.res.input;

public class ArrayOperations {

	public static int max(int... values) throws IllegalArgumentException {
		if (values == null) {
			throw new IllegalArgumentException("Values cannot be null!");
		}
		int res = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > res) {
				res = values[i];
			}
		}
		return res;
	}

	public static double sum(double... values) {
		double res = 0.0;
		for (double v : values) {
			res += v;
		}
		return res;
	}

	public static String concat(String separator, String... values) throws NullPointerException,
			IllegalArgumentException {
		if (separator == null) {
			throw new NullPointerException("Separator cannot be null!");
		}
		if (values == null) {
			throw new NullPointerException("Values cannot be null!");
		}
		if (separator.isEmpty()) {
			throw new IllegalArgumentException("Separator cannot be empty!");
		}
		if (values.length == 0) {
			throw new IllegalArgumentException("Values cannot be empty!");
		}
		for (String str : values) {
			if (str == null || str.isEmpty()) {
				throw new IllegalArgumentException("There cannot be empty strings in array!");
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String str : values) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(str);
		}
		return sb.toString();
	}

}
