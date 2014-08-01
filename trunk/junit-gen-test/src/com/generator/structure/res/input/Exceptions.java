package com.generator.structure.res.input;

import java.io.IOException;

public class Exceptions {
	
	public static void alwaysThrowsException(int a, int b) {
		throw new NullPointerException();
	}
	
	public static void thowsCheckedException(int a, int b) throws IOException {
		throw new IOException();
	}
	
	public static void throwsWarnedUncheckedException(int a, int b) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}
	
	public static double squareRoot(int a) {
		if (a < 0) {
			throw new IllegalArgumentException("A cannot be negative!");
		}
		return Math.sqrt(a);
	}
	
}
