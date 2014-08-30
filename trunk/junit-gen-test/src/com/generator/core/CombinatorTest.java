package com.generator.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.generator.core.Combinator;
import com.generator.core.util.FileUtil;

public class CombinatorTest {

	@Test
	public void testCombine() throws IOException {

		List<List<Object>> categories = new ArrayList<List<Object>>();
		categories.add(Arrays.asList(new Object[] { "-1", "+1" }));
		categories.add(Arrays.asList(new Object[] { "A", "B", "C" }));
		categories.add(Arrays.asList(new Object[] { "x", "y", "z" }));
		List<List<Object>> combinations = Combinator.combine(categories);

		StringBuilder actual = new StringBuilder();
		for (List<Object> list : combinations) {
			actual.append(list.toString()).append("\r\n");
		}
		File expectedDir = JUnitGeneratorTest.getExpectedDir();
		String expectedText = FileUtil.fileToText(new File(expectedDir, "combinator1.txt"));
		
		Assert.assertEquals(expectedText, actual.toString());
	}

}
