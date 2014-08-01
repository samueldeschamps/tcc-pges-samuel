package com.generator.structure.res.input;

public class Calculator {
	
	public static int sum(int a, int b) throws IllegalArgumentException {
		if (a < 0) {
			throw new IllegalArgumentException("A cannot be negative!");
		}
		return a + b;
	}
	
	public static int subtract(int a, int b) {
		return a - b;
	}
	
	public static int multiply(int a, int b) {
		return a * b;
	}
	
	public static int divide(int a, int b) {
		return a / b;
	}
	
	public static boolean isPrime(int number) {
		for (int i = 2; i < number - 1; ++i) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

}
