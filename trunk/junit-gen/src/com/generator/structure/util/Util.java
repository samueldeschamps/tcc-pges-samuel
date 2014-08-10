package com.generator.structure.util;

import java.io.InputStream;
import java.lang.reflect.Array;
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

}