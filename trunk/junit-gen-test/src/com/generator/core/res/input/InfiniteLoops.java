package com.generator.core.res.input;

public class InfiniteLoops {

	public static int beEternal(int xla) {
		if (xla == 0) {
			boolean stop = false;
			while (!stop) {
				xla = xla + 1;
			}
			return xla;
		} else {
			return xla + 1;
		}
	}

}
