package com.generator.core.util;

import java.io.IOException;

public class Log {

	public static synchronized void info(String message) {
		System.out.println(message);
	}

	public static synchronized void warning(String message) {
		System.out.println(message);
	}

	public static synchronized void warning(String message, IOException ex) {
		System.out.println(message);
		System.out.println(ex.getMessage());
	}

	public static synchronized void error(String message, Throwable ex) {
		System.out.println(message);
		System.out.println(ex.getMessage());
	}

	public static synchronized void error(String message) {
		System.out.println(message);
	}

	public static synchronized void debug(String message) {
		System.out.println(message);
	}

}
