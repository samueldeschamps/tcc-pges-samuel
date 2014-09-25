package com.generator.core.codeexplorers;

import japa.compiler.nodes.binding.binary.BinaryMethodBinding;
import japa.compiler.nodes.binding.source.SourceMethodBinding;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.generator.core.CallNode;
import com.generator.core.util.Log;

public class MethodCallFinder {

	public static Map<Method, CallNode> getCalledMethods(CompilationUnit unit) {
		Map<Method, Set<Method>> callHierarchy = new LinkedHashMap<>();
		InnerFinder visitor = new InnerFinder();
		unit.accept(visitor, callHierarchy);
		return mountTrees(callHierarchy);
	}

	private static Map<Method, CallNode> mountTrees(Map<Method, Set<Method>> callHierarchy) {
		Map<Method, CallNode> res = new LinkedHashMap<>();
		for (Entry<Method, Set<Method>> entry : callHierarchy.entrySet()) {
			CallNode node = new CallNode(entry.getKey());
			addCalleds(callHierarchy, node, entry.getValue());
			res.put(entry.getKey(), node);
		}
		return res;
	}

	private static void addCalleds(Map<Method, Set<Method>> map, CallNode parent, Set<Method> calleds) {
		for (Method m : calleds) {
			if (parent.isAncestor(m)) {
				continue;
			}
			CallNode child = new CallNode(m, parent);
			if (map.containsKey(m)) {
				addCalleds(map, child, map.get(m));
			}
			parent.addCalled(child);
		}
	}

	private static class InnerFinder extends ReflectionVisitorAdapter<Map<Method, Set<Method>>> {

		private Method currMethod;

		@Override
		public void visit(MethodDeclaration n, Map<Method, Set<Method>> arg) {
			this.currMethod = getReflectMethod(n);
			arg.put(currMethod, new LinkedHashSet<Method>());
			super.visit(n, arg);
		}

		@Override
		public void visit(MethodCallExpr n, Map<Method, Set<Method>> arg) {
			if (currMethod == null) {
				// Can be an var initialization expression.
				return;
			}
			Object data = n.getData();
			Method calledMethod = null;
			if (data instanceof BinaryMethodBinding) {
				calledMethod = ((BinaryMethodBinding) data).method;
			} else if (data instanceof SourceMethodBinding) {
				calledMethod = getReflectMethod(((SourceMethodBinding) data));
			}
			if (calledMethod != null) {
				// FIXME Remove this after resolving coverage/classloader issues
				// with java.lang. classes:
				if (!isProhibitedPackage(calledMethod.getDeclaringClass().getPackage())) {
					// TODO Improve performance:
					Set<Method> calleds = arg.get(currMethod);
					calleds.add(calledMethod);
				}
			} else {
				Log.warning("Could not find called method in expression '" + n.toString() + "'.");
			}
			super.visit(n, arg);
		}

		private boolean isProhibitedPackage(Package pkg) {
			String name = pkg.getName();
			if (name.startsWith("java.lang")) {
				return true;
			}
			if (name.startsWith("java.math")) {
				return true;
			}
			if (name.startsWith("java.io")) {
				return true;
			}
			return false;
		}

	}

}
