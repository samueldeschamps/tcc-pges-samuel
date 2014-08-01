package com.generator.structure;

import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import com.generator.structure.valuegenerators.common.IntegerCommonValues;
import com.generator.structure.valuegenerators.common.StringCommonValues;

public class JUnitGenerator {

	private List<Class<?>> targetClasses = new ArrayList<>();
	private List<Method> targetMethods = new ArrayList<>();
	private String outputDir;
	private String testPackageName;
	private List<Class<? extends Throwable>> bugExceptions = new ArrayList<>();
	private double minCoverageRate; // Value between 0.0 and 1.0
	private int eternalLoopTimetout; // Value in seconds.
	private ExceptionsExpected expectionsStrategy;
	private ValueGeneratorRegistry valueGenerators = new ValueGeneratorRegistry();
	private String result;

	public JUnitGenerator() {
		loadDefaultConfig();
	}

	private void loadDefaultConfig() {
		loadDefaultBugExceptions();
		loadDefaultParamGenerators();
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
		valueGenerators.register(Integer.class, IntegerCommonValues.class);
		valueGenerators.register(int.class, IntegerCommonValues.class);
		valueGenerators.register(String.class, StringCommonValues.class);
	}

	public void execute() {
		validateParameters();
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
		
		Map<Method, List<TestCaseData>> cases = new LinkedHashMap<>();
		if (method != null) {
			cases.put(method, getTestCasesValues(targetClass, method));
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
				cases.put(m, getTestCasesValues(targetClass, m));
			}
		}
		
		TestCodeGenerator codeGen = new TestCodeGenerator();
		codeGen.setTargetClass(targetClass);
		codeGen.setCases(cases);
		codeGen.setTestPackageName(testPackageName);
		codeGen.execute();
		CompilationUnit unit = codeGen.getResult();
		return unit;
	}

	private List<TestCaseData> getTestCasesValues(Class<?> targetClass, Method method) {
		TestDataGenerator generator = new TestDataGenerator(this, method);
		generator.execute();
		return generator.getResult();
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

	public double getMinCoverageRate() {
		return minCoverageRate;
	}

	public void setMinCoverageRate(double minCoverageRate) {
		this.minCoverageRate = minCoverageRate;
	}

	public int getEternalLoopTimetout() {
		return eternalLoopTimetout;
	}

	public void setEternalLoopTimetout(int eternalLoopTimetout) {
		this.eternalLoopTimetout = eternalLoopTimetout;
	}

	public ExceptionsExpected getExpectionsStrategy() {
		return expectionsStrategy;
	}

	public void setExpectionsStrategy(ExceptionsExpected expectionsStrategy) {
		this.expectionsStrategy = expectionsStrategy;
	}

	public String getResult() {
		return result;
	}

	ValueGeneratorRegistry getValueGenerators() {
		return valueGenerators;
	}

}
