package com.generator.structure;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.generator.structure.res.input.ArrayOperations;
import com.generator.structure.res.input.Enumerations;
import com.generator.structure.res.input.Exceptions;
import com.generator.structure.res.input.PrimitiveOperations;
import com.generator.structure.res.input.SimpleIntCalculator;
import com.generator.structure.res.input.ValidaCPF;
import com.generator.structure.res.input.ValidaCpfCnpj;
import com.generator.structure.util.FileUtil;

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
		File result = new File(projectDir, "src.com.generator.structure.res.expected".replace(".", sep));
		return result;
	}

	private void testGeneration(Class<?> clazz) {
		try {
			String tempDirName = tempDir.getRoot().getCanonicalPath();

			JUnitGenerator gen = new JUnitGenerator();
			gen.addTargetClass(clazz);
			gen.setOutputDir(tempDirName);
			gen.setTestPackageName("com.generator.structure.res.expected");
			gen.execute();

			String testFileName = clazz.getSimpleName() + "Test.java";
			String actual = FileUtil.fileToText(new File(tempDirName, testFileName));
			File expectedFile = new File(expectedDir, testFileName);
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
						String msg = "Confirm overwrite " + testFileName + "?";
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
		} catch (IOException ex) {
			throw new RuntimeException(ex);
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

}
