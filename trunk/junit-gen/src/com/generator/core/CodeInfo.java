package com.generator.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.generator.core.util.Log;
import com.generator.core.util.Util;

import japa.compiler.CompilationException;
import japa.compiler.Main;
import japa.parser.ast.CompilationUnit;

public class CodeInfo {
	
	private List<CompilationUnit> units = new ArrayList<>();
	private Map<Method, CallNode> callHierarchies = new LinkedHashMap<>();
	
	public CallNode getCallHierarchy(Method method) {
		return callHierarchies.get(method);
	}
	
	public void parseCode(Class<?> targetClass) {
		File targetFile = Util.getSourceFile(targetClass);
		try {
			// TODO Add Classpath andSourcepath!
			
			File[] sourcePath = Main.getBootClasspath();
			CompilationUnit unit = japa.compiler.Compiler.compile(sourcePath, sourcePath, targetFile, false);
			units.add(unit);
			callHierarchies.putAll(MethodCallFinder.getCalledMethods(unit));

		} catch (CompilationException e) {
			Log.error(String.format("Error parsing class '%s.'", targetClass.getSimpleName()), e);
		}
		
	}
	
}
