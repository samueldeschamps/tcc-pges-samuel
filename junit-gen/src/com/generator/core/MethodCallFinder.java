package com.generator.core;

import japa.compiler.nodes.binding.binary.BinaryMethodBinding;
import japa.compiler.nodes.binding.source.AbstractSourceClassBinding;
import japa.compiler.nodes.binding.source.SourceClassBinding;
import japa.compiler.nodes.binding.source.SourceMethodBinding;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

	private static class InnerFinder extends VoidVisitorAdapter<Map<Method, Set<Method>>> {

		private SourceClassBinding currClass;
		private Method currMethod;

		@Override
		public void visit(ClassOrInterfaceDeclaration n, Map<Method, Set<Method>> arg) {
			this.currClass = (SourceClassBinding) n.getData();
			super.visit(n, arg);
		}

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

		private Method getReflectMethod(MethodDeclaration methodDecl) {
			String methodName = methodDecl.getName();

			Class<?> clazz;
			try {
				clazz = Class.forName(currClass.getName());
			} catch (ClassNotFoundException e) {
				Log.error(String.format("Could not load class '%s'.", currClass.getName()));
				return null;
			}
			List<Parameter> params = methodDecl.getParameters();
			Method result = findMethodInClass(clazz, methodName, getTypes(params));
			if (result == null) {
				Log.error(String.format("Could not find method '%s' in class '%s'.", methodName, currClass.getName()));
			}
			return result;
		}

		private Method getReflectMethod(SourceMethodBinding srcBinding) {
			String methodName = srcBinding.getMethodDeclaration().getName();
			AbstractSourceClassBinding srcClass = srcBinding.getSourceClassBinding();
			String className = srcClass.getName();
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				Log.error(String.format("Could not load class '%s'.", className));
				return null;
			}
			List<Parameter> params = srcBinding.getMethodDeclaration().getParameters();
			return findMethodInClass(clazz, methodName, getTypes(params));
		}

		private Type[] getTypes(List<Parameter> params) {
			Type[] res = new Type[params.size()];
			for (int i = 0; i < params.size(); ++i) {
				Parameter param = params.get(i);
				if (param.isVarArgs()) {
					// TODO Support int[]...
					res[i] = new ReferenceType(param.getType(), 1);
				} else {
					res[i] = param.getType();
				}
			}
			return res;
		}

		private Method findMethodInClass(Class<?> clazz, String methodName, Type[] argTypes) {
			Method[] methods = clazz.getDeclaredMethods();
			for (Method m : methods) {
				if (!m.getName().equals(methodName)) {
					continue;
				}
				Type[] paramTypes = JavaParserAdapter.javaTypesToParserTypes(m.getParameterTypes());
				if (!typesMatch(paramTypes, argTypes)) {
					continue;
				}
				return m;
			}
			return null;
		}

		private boolean typesMatch(Type[] t1, Type[] t2) {
			replaceReferences(t1);
			replaceReferences(t2);
			return Arrays.equals(t1, t2);
		}

		private void replaceReferences(Type[] types) {
			for (int i = 0; i < types.length; ++i) {
				types[i] = replaceReference(types[i]);
			}
		}

		private Type replaceReference(Type type) {
			if (type instanceof ReferenceType) {
				ReferenceType refType = (ReferenceType) type;
				if (refType.getArrayCount() == 0) {
					return refType.getType();
				} else {
					refType.setType(replaceReference(refType.getType()));
					return type;
				}
			} else {
				return type;
			}
		}

	}

}
