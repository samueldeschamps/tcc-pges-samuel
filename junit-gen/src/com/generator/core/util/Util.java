package com.generator.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Util {

	private static final String[] UVOWELS = new String[] { "A", "E", "I", "O", "U" };
	private static final String[] LVOWELS = new String[] { "a", "e", "i", "o", "u" };

	public static boolean startsWithVowel(String str) {
		for (String vowel : UVOWELS) {
			if (str.startsWith(vowel)) {
				return true;
			}
		}
		for (String vowel : LVOWELS) {
			if (str.startsWith(vowel)) {
				return true;
			}
		}
		return false;
	}

	public static int intPow(int base, int exp) {
		if (exp < 0) {
			throw new IllegalArgumentException("Negative exponents not supported!");
		}
		int res = 1;
		while (exp-- > 0) {
			res *= base;
		}
		return res;
	}

	public static InputStream getClassBytecodeAsStream(Class<?> clazz) {
		final String resource = '/' + clazz.getName().replace('.', '/') + ".class";
		return clazz.getResourceAsStream(resource);
	}

	public static InputStream getClassSourceAsStream(Class<?> clazz) {
		File sourceFile = getSourceFile(clazz);
		try {
			return new FileInputStream(sourceFile);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public static File getSourceFile(Class<?> clazz) {
		String srcDir = getSourceDirLocation(clazz);
		if (!srcDir.endsWith("/")) {
			srcDir += "/";
		}
		String javaFilePath = clazz.getName().replace('.', '/') + ".java";
		return new File(srcDir + javaFilePath);
	}

	public static String getSourceDirLocation(Class<?> clazz) {
		return getSourceDirLocation(clazz, "src");
	}

	public static String getSourceDirLocation(Class<?> clazz, String srcDirSimpleName) {
		final String resource = '/' + clazz.getName().replace('.', '/') + ".class";
		final String path = clazz.getResource(resource).getPath();
		final String projectPath = path.substring(0, path.length() - resource.length() - "bin".length());
		return projectPath + srcDirSimpleName;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] arrayConcat(Class<T> elementType, T[]... arrays) {
		int totalLen = 0;
		for (T[] array : arrays) {
			totalLen += array.length;
		}
		T[] result = (T[]) Array.newInstance(elementType, totalLen);
		int pos = 0;
		for (T[] array : arrays) {
			System.arraycopy(array, 0, result, pos, array.length);
			pos += array.length;
		}
		return result;
	}

	public static String upFirstChar(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		if (Character.isUpperCase(str.charAt(0))) {
			return str;
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<Class<?>, Class<?>>();
	static {
		WRAPPERS.put(boolean.class, Boolean.class);
		WRAPPERS.put(byte.class, Byte.class);
		WRAPPERS.put(char.class, Character.class);
		WRAPPERS.put(double.class, Double.class);
		WRAPPERS.put(float.class, Float.class);
		WRAPPERS.put(int.class, Integer.class);
		WRAPPERS.put(long.class, Long.class);
		WRAPPERS.put(short.class, Short.class);
		WRAPPERS.put(void.class, Void.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> primitiveToWrapper(Class<T> c) {
		return (Class<T>) WRAPPERS.get(c);
	}

	public static double round(double x, int scale) {
		return round(x, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	public static double round(double x, int scale, int roundingMethod) {
		try {
			return (new BigDecimal(Double.toString(x)).setScale(scale, roundingMethod)).doubleValue();
		} catch (NumberFormatException ex) {
			if (Double.isInfinite(x)) {
				return x;
			} else {
				return Double.NaN;
			}
		}
	}

}
