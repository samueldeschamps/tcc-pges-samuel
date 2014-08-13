package com.generator.core.util;

import java.io.IOException;

public class Log {

	public static void info(String message) {
		System.out.println(message);
	}

	public static void warning(String message) {
		System.out.println(message);
	}

	public static void warning(String message, IOException ex) {
		System.out.println(message);
		System.out.println(ex.getMessage());
	}

	public static void error(String message, Throwable ex) {
		System.out.println(message);
		System.out.println(ex.getMessage());
	}

	public static void error(String message) {
		System.out.println(message);
	}

	public static void debug(String message) {
		System.out.println(message);
	}

}
