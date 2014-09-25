package com.generator.core.res.input;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Exceptions {
	
	public static void throwException(int a, int b) {
		throw new NullPointerException();
	}
	
	public static void throwDeclaredCheckedException(int a, int b) throws IOException {
		throw new IOException();
	}
	
	public static void throwDeclaredUncheckedException(int a, int b) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}
	
	public static double squareRoot(int a) {
		if (a < 0) {
			throw new IllegalArgumentException("A cannot be negative!");
		}
		return Math.sqrt(a);
	}
	
	public static double squareRootDeclared(int a) throws IllegalArgumentException {
		if (a < 0) {
			throw new IllegalArgumentException("A cannot be negative!");
		}
		return Math.sqrt(a);
	}
	
	public static int divide(int a, int b) throws Exception {
		if (b == 0) {
			throw new Exception("Division by zero.");
		}
		return a / b;
	}
	
	// Just to use a different exception and see if its imported
	public static int divide2(int a, int b) throws FileNotFoundException {
		if (b == 0) {
			throw new FileNotFoundException("Division by zero.");
		}
		return a / b;
	}
	
}
