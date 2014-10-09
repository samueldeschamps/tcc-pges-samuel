package com.generator.core.codeexplorers;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.StringLiteralExpr;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.generator.core.util.Util;

public class StringLiteralsFinder {

	public static Map<? extends Method, ? extends Set<String>> findStringLiterals(CompilationUnit unit) {
		Map<Method, Set<String>> map = new LinkedHashMap<>();
		InnerFinder finder = new InnerFinder();
		unit.accept(finder, map);
		return map;
	}

	private static class InnerFinder extends ReflectionVisitorAdapter<Map<Method, Set<String>>> {

		private Method currMethod = null;

		@Override
		public void visit(MethodDeclaration n, Map<Method, Set<String>> arg) {
			// Pode haver um método dentro de outro (inner classes...)
			Method backup = currMethod;
			currMethod = getReflectMethod(n);
			super.visit(n, arg);
			currMethod = backup;
		}

		@Override
		public void visit(StringLiteralExpr n, Map<Method, Set<String>> arg) {
			super.visit(n, arg);
			if (currMethod != null) {
				Util.putSetItem(arg, currMethod, n.getValue());
			}
		}
	}

}
