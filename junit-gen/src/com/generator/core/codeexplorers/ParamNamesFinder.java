package com.generator.core.codeexplorers;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParamNamesFinder {

	public static Map<Method, List<String>> findParamNames(CompilationUnit unit) {
		Map<Method, List<String>> map = new LinkedHashMap<>();
		InnerFinder finder = new InnerFinder();
		unit.accept(finder, map);
		return map;
	}

	private static class InnerFinder extends ReflectionVisitorAdapter<Map<Method, List<String>>> {

		@Override
		public void visit(MethodDeclaration n, Map<Method, List<String>> arg) {
			Method method = getReflectMethod(n);
			if (method != null) {
				List<String> names = new ArrayList<>();
				List<Parameter> params = n.getParameters();
				if (params != null) {
					for (Parameter param : params) {
						names.add(param.getId().getName());
					}
				}
				arg.put(method, names);
			}
			super.visit(n, arg);
		}
	}

}
