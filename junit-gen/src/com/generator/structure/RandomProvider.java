package com.generator.structure;

import java.util.Random;

public class RandomProvider {

	public static boolean FIXED_SEED = false;

	public static Random createRandom() {
		if (FIXED_SEED) {
			return new Random(0L);
		} else {
			return new Random();
		}
	}

}
