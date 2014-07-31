package com.generator.structure;

import org.junit.Test;

import com.target.statik.Calculator;

public class JUnitGeneratorTest {
	
	@Test
	public void testCalculator() {
		
		JUnitGenerator gen = new JUnitGenerator();
		gen.addTargetClass(Calculator.class);
		gen.execute();
		String result = gen.getResult();
		System.out.println(result);
		
	}

}
