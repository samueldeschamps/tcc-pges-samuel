package com.generator.core;

import japa.compiler.CompilationException;
import japa.compiler.Compiler;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.generator.core.codeexplorers.MethodCallFinder;
import com.generator.core.codeexplorers.ParamNamesFinder;
import com.generator.core.codeexplorers.StringLiteralsFinder;
import com.generator.core.util.Log;
import com.generator.core.util.Util;

// TODO Improve performance parsing all files once before starting to generate tests.
public class CodeInfo {

	private File[] compileDir;
	private Map<Method, CallNode> callHierarchies = new LinkedHashMap<>();
	private Map<Method, List<String>> paramNames = new LinkedHashMap<>();
	private Map<Method, Set<String>> stringLiterals = new LinkedHashMap<>();

	public CodeInfo(File[] compileDir) {
		this.compileDir = compileDir;
	}

	public CallNode getCallHierarchy(Method method) {
		return callHierarchies.get(method);
	}

	public List<String> getParamNames(Method method) {
		return paramNames.get(method);
	}

	public Set<String> getStringLiterals(Method method) {
		return stringLiterals.get(method);
	}

	public void parseCode(Class<?> targetClass) {
		File targetFile = Util.getSourceFile(targetClass);
		try {
			File[] classPath = getJavaClasspath();
			File[] srcPath = getSourcePath(targetClass);

			// Compiles all the files located in target class' project:
			Compiler compiler = new Compiler(classPath, srcPath);
			compiler.compile(getCompileDir(targetClass));
			CompilationUnit unit = compiler.getUnit(targetFile);

			callHierarchies.putAll(MethodCallFinder.getCalledMethods(unit));
			paramNames.putAll(ParamNamesFinder.findParamNames(unit));
			stringLiterals.putAll(StringLiteralsFinder.findStringLiterals(unit));

		} catch (CompilationException e) {
			Log.error(String.format("Error parsing class '%s.'", targetClass.getSimpleName()), e);
		}
	}

	private File[] getCompileDir(Class<?> targetClass) {
		if (compileDir == null || compileDir.length == 0) {
			// If not specified, compiles all the 'src' directory:
			compileDir = getSourcePath(targetClass);
		}
		return compileDir;
	}

	private File[] getSourcePath(Class<?> targetClass) {
		return new File[] { new File(Util.getSourceDirLocation(targetClass)) };
	}

	public static File[] getJavaClasspath() {
		String javaHome = System.getProperty("java.home");
		File bootDir = new File(javaHome, "lib");
		if (!bootDir.exists()) {
			return null;
		}
		return bootDir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".jar") || name.endsWith(".zip");
			}

		});
	}

}
