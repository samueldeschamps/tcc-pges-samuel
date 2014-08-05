package com.generator.structure.res.input;

public class PrimitiveOperations {
	
	public static boolean xor(boolean a, boolean b) {
		return a ^ b;
	}
	
	public static byte byteSum(byte a, byte b) {
		return (byte) (a + b);
	}
	
	public static short shortSum(short a, short b) {
		return (short) (a + b);
	}
	
	public static int intSum(int a, int b) {
		return a + b;
	}
	
	public static long longSum(long a, long b) {
		return a + b;
	}
	
	public static float floatSum(float a, float b) {
		return a + b;
	}
	
	public static double doubleSum(double a, double b) {
		return a + b;
	}
	
	public static char upCase(char c) {
		return Character.toUpperCase(c);
	}
	
	public static int hashCode(boolean a, byte b, short c, char d, int e, long f, float g, double h) {
		int res = 0;
		res ^= Boolean.valueOf(a).hashCode();
		res ^= Byte.valueOf(b).hashCode();
		res ^= Short.valueOf(c).hashCode();
		res ^= Character.valueOf(d).hashCode();
		res ^= Integer.valueOf(e).hashCode();
		res ^= Long.valueOf(f).hashCode();
		res ^= Float.valueOf(g).hashCode();
		res ^= Double.valueOf(h).hashCode();
		return res;
	}

}
