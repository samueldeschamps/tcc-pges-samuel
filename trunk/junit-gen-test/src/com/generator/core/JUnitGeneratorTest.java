package com.generator.core;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.generator.core.res.input.ArrayOperations;
import com.generator.core.res.input.Enumerations;
import com.generator.core.res.input.Exceptions;
import com.generator.core.res.input.InfiniteLoops;
import com.generator.core.res.input.IntOperationsDefault;
import com.generator.core.res.input.IntOperationsSymbolic;
import com.generator.core.res.input.Monetary;
import com.generator.core.res.input.PrimitiveOperations;
import com.generator.core.res.input.SimpleIntCalculator;
import com.generator.core.res.input.ValidaCPF;
import com.generator.core.res.input.ValidaCpfCnpj;
import com.generator.core.util.FileUtil;
import com.generator.core.util.Util;
import com.generator.core.valuegenerators.symbolic.IntSymbolicValueGenerator;

public class JUnitGeneratorTest {

	private static final boolean REGENERATE = false;

	@Rule
	public TemporaryFolder tempDir = new TemporaryFolder();
	private static File expectedDir;

	@BeforeClass
	public static void beforeClass() {
		RandomProvider.FIXED_SEED = true;
		expectedDir = getExpectedDir();
	}

	public static File getExpectedDir() {
		String url = JUnitGeneratorTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File projectDir = new File(url).getParentFile();
		String sep = File.separator;
		File result = new File(projectDir, "src.com.generator.core.res.expected".replace(".", sep));
		return result;
	}

	private void testGeneration(Class<?> clazz) {
		try {
			String tempDirName = tempDir.getRoot().getCanonicalPath();
			JUnitGenerator gen = createGenerator(clazz, tempDirName);
			gen.execute();
			String testFileName = clazz.getSimpleName() + "Test.java";
			compareGeneratedFiles(clazz, testFileName);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private JUnitGenerator createGenerator(Class<?> clazz, String tempDirName) {
		JUnitGenerator gen = new JUnitGenerator();
		gen.addTargetClass(clazz);
		gen.setOutputDir(tempDirName);
		gen.setTestPackageName("com.generator.core.res.expected");
		gen.setCompileDir(new File[] { new File(Util.getSourceDirLocation(clazz) + "/com/generator/core/res/input") });
		return gen;
	}

	private void compareGeneratedFiles(Class<?> clazz, String expectedFileName) throws IOException, AssertionError {
		String tempDirName = tempDir.getRoot().getCanonicalPath();
		String actualFileName = clazz.getSimpleName() + "Test.java";
		String actual = FileUtil.fileToText(new File(tempDirName, actualFileName));
		File expectedFile = new File(expectedDir, expectedFileName);
		if (!expectedFile.exists()) {
			if (REGENERATE) {
				FileUtil.writeTextToFile(expectedFile, actual);
			} else {
				Assert.fail("File '" + expectedFile.getAbsolutePath() + "' does not exist.");
			}
		} else {
			String expected = FileUtil.fileToText(expectedFile);
			try {
				Assert.assertEquals(expected, actual);
			} catch (AssertionError ex) {
				if (REGENERATE) {
					String msg = "Confirm overwrite " + expectedFileName + "?";
					if (JOptionPane.showConfirmDialog(null, msg) == JOptionPane.YES_OPTION) {
						FileUtil.writeTextToFile(expectedFile, actual);
					} else {
						throw ex;
					}
				} else {
					throw ex;
				}
			}
		}
	}

	@Test
	public void testSimpleIntCalculator() {
		testGeneration(SimpleIntCalculator.class);
	}

	@Test
	public void testExceptions() {
		testGeneration(Exceptions.class);
	}

	@Test
	public void testValidaCPF() {
		testGeneration(ValidaCPF.class);
	}

	@Test
	public void testValidaCpfCnpj() {
		testGeneration(ValidaCpfCnpj.class);
	}

	@Test
	public void testEnumerations() {
		testGeneration(Enumerations.class);
	}

	@Test
	public void testPrimitiveOperations() {
		testGeneration(PrimitiveOperations.class);
	}

	@Test
	public void testArrayOperations() {
		testGeneration(ArrayOperations.class);
	}

	@Test
	public void testMonetary() {
		testGeneration(Monetary.class);
	}

	@Test
	public void testIntOperationsSymbolic() throws IOException {
		Class<?> clazz = IntOperationsSymbolic.class;
		String tempDirName = tempDir.getRoot().getCanonicalPath();
		JUnitGenerator gen = createGenerator(clazz, tempDirName);
		gen.getValueGenerators().unregisterAll();
		gen.getValueGenerators().register(int.class, IntSymbolicValueGenerator.class);
		gen.execute();
		compareGeneratedFiles(clazz, clazz.getSimpleName() + "Test.java");
	}

	@Test
	public void testIntOperationsDefault() {
		testGeneration(IntOperationsDefault.class);
	}
	
	@Test
	public void testInfiniteLoops() {
		testGeneration(InfiniteLoops.class);
	}

}
