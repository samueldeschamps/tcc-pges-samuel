package com.generator.core;

import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import com.generator.core.util.Log;
import com.generator.core.valuegenerators.common.BigDecimalCommonValues;
import com.generator.core.valuegenerators.common.BigIntegerCommonValues;
import com.generator.core.valuegenerators.common.ByteCommonValues;
import com.generator.core.valuegenerators.common.CharCommonValues;
import com.generator.core.valuegenerators.common.DoubleCommonValues;
import com.generator.core.valuegenerators.common.FloatCommonValues;
import com.generator.core.valuegenerators.common.IntegerCommonValues;
import com.generator.core.valuegenerators.common.LongCommonValues;
import com.generator.core.valuegenerators.common.ShortCommonValues;
import com.generator.core.valuegenerators.common.StringCommonValues;
import com.generator.core.valuegenerators.full.BooleanFullValues;
import com.generator.core.valuegenerators.full.ByteFullValues;
import com.generator.core.valuegenerators.full.CharFullValues;
import com.generator.core.valuegenerators.full.EnumFullValues;
import com.generator.core.valuegenerators.full.ShortFullValues;
import com.generator.core.valuegenerators.random.BigDecimalRandomValues;
import com.generator.core.valuegenerators.random.BigIntegerRandomValues;
import com.generator.core.valuegenerators.random.DoubleRandomValues;
import com.generator.core.valuegenerators.random.IntegerRandomValues;
import com.generator.core.valuegenerators.random.StringRandomValues;

public class JUnitGenerator {

	private List<Class<?>> targetClasses = new ArrayList<>();
	private List<Method> targetMethods = new ArrayList<>();
	private String outputDir;
	private String testPackageName;
	private List<Class<? extends Throwable>> bugExceptions = new ArrayList<>();
	private double[] minCovRatioPerMethod; // Values between 0.0 and 1.0. Indices represent method call depth.
	private double minCovRatioPerSucceededTestCase; // Value between 0.0 and 1.0
	private double minCovRatioPerFailedTestCase; // Value between 0.0 and 1.0
	private int minTestCasesPerMethod; //
	private int eternalLoopTimeout; // Value in seconds.
	private double doubleAssertPrecision; // Used in double assertives.
	private ExceptionsStrategy exceptionsStrategy;
	private ValueGeneratorRegistry valueGenerators = new ValueGeneratorRegistry();
	private File[] compileDir;
	private CodeInfo codeInfo;
	private String result;

	public JUnitGenerator() {
		loadDefaultConfig();
	}

	private void loadDefaultConfig() {
		loadDefaultBugExceptions();
		loadDefaultParamGenerators();

		exceptionsStrategy = ExceptionsStrategy.ASSERT_WHEN_DECLARED;
		doubleAssertPrecision = 0.00000001;
		minCovRatioPerFailedTestCase = 0.01;
		minCovRatioPerSucceededTestCase = 0.01;
		minCovRatioPerMethod = new double[] { 0.999, 0.9, 0.5 };
		minTestCasesPerMethod = 3;
	}

	private void loadDefaultBugExceptions() {
		addBugException(AssertionError.class);
		addBugException(IllegalStateException.class);
		addBugException(NullPointerException.class);
		addBugException(ArithmeticException.class);
		addBugException(ArrayIndexOutOfBoundsException.class);
		addBugException(StringIndexOutOfBoundsException.class);
		addBugException(OperationNotSupportedException.class);
		// TODO Include more exceptions.
	}

	private void loadDefaultParamGenerators() {
		loadPrimitiveParamGenerators();

		valueGenerators.register(Enum.class, EnumFullValues.class);

		valueGenerators.register(String.class, StringCommonValues.class);
		valueGenerators.register(String.class, StringRandomValues.class);

		valueGenerators.register(BigDecimal.class, BigDecimalCommonValues.class);
		valueGenerators.register(BigDecimal.class, BigDecimalRandomValues.class);

		valueGenerators.register(BigInteger.class, BigIntegerCommonValues.class);
		valueGenerators.register(BigInteger.class, BigIntegerRandomValues.class);
	}

	private void loadPrimitiveParamGenerators() {
		valueGenerators.register(boolean.class, BooleanFullValues.class);
		valueGenerators.register(Boolean.class, BooleanFullValues.class);

		valueGenerators.register(byte.class, ByteCommonValues.class);
		valueGenerators.register(Byte.class, ByteCommonValues.class);
		valueGenerators.register(byte.class, ByteFullValues.class);
		valueGenerators.register(Byte.class, ByteFullValues.class);

		valueGenerators.register(short.class, ShortCommonValues.class);
		valueGenerators.register(Short.class, ShortCommonValues.class);
		valueGenerators.register(short.class, ShortFullValues.class);
		valueGenerators.register(Short.class, ShortFullValues.class);

		valueGenerators.register(char.class, CharCommonValues.class);
		valueGenerators.register(Character.class, CharCommonValues.class);
		valueGenerators.register(char.class, CharFullValues.class);
		valueGenerators.register(Character.class, CharFullValues.class);

		valueGenerators.register(int.class, IntegerCommonValues.class);
		valueGenerators.register(Integer.class, IntegerCommonValues.class);
		valueGenerators.register(int.class, IntegerRandomValues.class);
		valueGenerators.register(Integer.class, IntegerRandomValues.class);

		valueGenerators.register(long.class, LongCommonValues.class);
		valueGenerators.register(Long.class, LongCommonValues.class);

		valueGenerators.register(float.class, FloatCommonValues.class);
		valueGenerators.register(Float.class, FloatCommonValues.class);

		valueGenerators.register(double.class, DoubleCommonValues.class);
		valueGenerators.register(Double.class, DoubleCommonValues.class);
		valueGenerators.register(double.class, DoubleRandomValues.class);
		valueGenerators.register(Double.class, DoubleRandomValues.class);
	}

	public void execute() {
		validateParameters();
		codeInfo = new CodeInfo(compileDir);
		List<CompilationUnit> testClasses = new ArrayList<>();
		if (!targetClasses.isEmpty()) {
			for (Class<?> clazz : targetClasses) {
				CompilationUnit unit = generate(clazz, null);
				testClasses.add(unit);
			}
		} else if (!targetMethods.isEmpty()) {
			for (Method method : targetMethods) {
				CompilationUnit unit = generate(method.getDeclaringClass(), method);
				testClasses.add(unit);
			}
		}
		writeResult(testClasses);
	}

	private void validateParameters() {
		if (targetClasses.isEmpty() && targetMethods.isEmpty()) {
			throw new IllegalArgumentException("No target class or method informed.");
		}
		if (!targetClasses.isEmpty() && !targetMethods.isEmpty()) {
			throw new IllegalArgumentException(
					"Both targetClasses and targetMethods are filled. Please inform only one of them.");
		}
	}

	private void writeResult(List<CompilationUnit> testClasses) {
		if (outputDir == null || outputDir.isEmpty()) {
			// If OuputDir is empty, the result is printed as string (for tests,
			// for example).
			writeResultAsString(testClasses);
		} else {
			writeResultAsFiles(testClasses);
		}
	}

	private void writeResultAsString(List<CompilationUnit> testClasses) {
		StringBuilder sb = new StringBuilder();
		for (CompilationUnit unit : testClasses) {
			if (sb.length() > 0) {
				sb.append("\n----------------------------------------------\n");
			}
			sb.append(unit.toString());
		}
		result = sb.toString();
	}

	private void writeResultAsFiles(List<CompilationUnit> testClasses) {
		for (CompilationUnit unit : testClasses) {
			String fileName = unit.getTypes().get(0).getName() + ".java";
			File file = new File(outputDir, fileName);
			try {
				FileWriter writer = new FileWriter(file);
				try {
					writer.write(unit.toString());
				} finally {
					writer.close();
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private CompilationUnit generate(Class<?> targetClass, Method method) {

		codeInfo.parseCode(targetClass);

		Map<Method, List<TestCaseData>> cases = new LinkedHashMap<>();
		if (method != null) {
			cases.put(method, generateTestCasesValues(method));
		} else {
			// TODO Alterar para obter a ordem original dos métodos na classe
			Method[] methods = targetClass.getDeclaredMethods();
			Arrays.sort(methods, new Comparator<Method>() {

				@Override
				public int compare(Method o1, Method o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			for (Method m : methods) {
				// TODO Ignore constructors
				if (!Modifier.isPublic(m.getModifiers())) {
					continue;
				}
				cases.put(m, generateTestCasesValues(m));
			}
		}

		CodeGenerator codeGen = new CodeGenerator(this);
		codeGen.setTargetClass(targetClass);
		codeGen.setCases(cases);
		codeGen.setTestPackageName(testPackageName);
		codeGen.execute();
		return codeGen.getResult();
	}

	private List<TestCaseData> generateTestCasesValues(Method method) {
		String msg = "Generating test cases for method '%s.%s'...";
		Log.info(String.format(msg, method.getDeclaringClass().getSimpleName(), method.getName()));
		TestCaseGenerator generator = new TestCaseGenerator(this, method);
		return generator.execute();
	}

	public List<Class<?>> getTargetClasses() {
		return Collections.unmodifiableList(targetClasses);
	}

	// TODO Passar o caminho completo do arquivo em vez do nome da classe
	public void addTargetClass(String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		addTargetClass(clazz);
	}

	public void addTargetClass(Class<?> clazz) {
		this.targetClasses.add(clazz);
	}

	public List<Method> getTargetMethods() {
		return Collections.unmodifiableList(targetMethods);
	}

	public void addTargetMethod(Method method) {
		this.targetMethods.add(method);
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getTestPackageName() {
		return testPackageName;
	}

	public void setTestPackageName(String testPackageName) {
		this.testPackageName = testPackageName;
	}

	public List<Class<? extends Throwable>> getBugExceptions() {
		return Collections.unmodifiableList(bugExceptions);
	}

	public void addBugException(Class<? extends Throwable> bugException) {
		this.bugExceptions.add(bugException);
	}

	public double[] getMinCovRatioPerMethod() {
		return minCovRatioPerMethod;
	}

	public void setMinCovRatioPerMethod(double[] minCovRatioPerMethod) {
		this.minCovRatioPerMethod = minCovRatioPerMethod;
	}

	public double getMinCovRatioPerSucceededTestCase() {
		return minCovRatioPerSucceededTestCase;
	}

	public void setMinCovRatioPerSucceededTestCase(double minCovRatioPerSucceededTestCase) {
		this.minCovRatioPerSucceededTestCase = minCovRatioPerSucceededTestCase;
	}

	public double getMinCovRatioPerFailedTestCase() {
		return minCovRatioPerFailedTestCase;
	}

	public void setMinCovRatioPerFailedTestCase(double minCovRatioPerFailedTestCase) {
		this.minCovRatioPerFailedTestCase = minCovRatioPerFailedTestCase;
	}

	public int getMinTestCasesPerMethod() {
		return minTestCasesPerMethod;
	}

	public void setMinTestCasesPerMethod(int minTestCasesPerMethod) {
		this.minTestCasesPerMethod = minTestCasesPerMethod;
	}

	public int getEternalLoopTimeout() {
		return eternalLoopTimeout;
	}

	public void setEternalLoopTimeout(int eternalLoopTimetout) {
		this.eternalLoopTimeout = eternalLoopTimetout;
	}

	public double getDoubleAssertPrecision() {
		return doubleAssertPrecision;
	}

	public void setDoubleAssertPrecision(double doubleAssertPrecision) {
		this.doubleAssertPrecision = doubleAssertPrecision;
	}

	public ExceptionsStrategy getExceptionsStrategy() {
		return exceptionsStrategy;
	}

	public void setExceptionsStrategy(ExceptionsStrategy exceptionsStrategy) {
		this.exceptionsStrategy = exceptionsStrategy;
	}
	
	public void setCompileDir(File[] compileDir) {
		this.compileDir = compileDir;
	}
	
	public String getResult() {
		return result;
	}

	ValueGeneratorRegistry getValueGenerators() {
		return valueGenerators;
	}
	
	int getMaxCoverageDepth() {
		return minCovRatioPerMethod.length - 1;
	}
	
	CodeInfo getCodeInfo() {
		return codeInfo;
	}

}
