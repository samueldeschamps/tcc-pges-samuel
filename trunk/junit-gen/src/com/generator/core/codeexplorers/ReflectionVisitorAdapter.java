package com.generator.core.codeexplorers;

import japa.compiler.nodes.binding.source.AbstractSourceClassBinding;
import japa.compiler.nodes.binding.source.SourceClassBinding;
import japa.compiler.nodes.binding.source.SourceMethodBinding;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.generator.core.JavaParserAdapter;
import com.generator.core.util.Log;

public class ReflectionVisitorAdapter<A> extends VoidVisitorAdapter<A> {

	private SourceClassBinding currClass;

	@Override
	public void visit(ClassOrInterfaceDeclaration n, A arg) {
		this.currClass = (SourceClassBinding) n.getData();
		super.visit(n, arg);
	}

	protected Method getReflectMethod(MethodDeclaration methodDecl) {
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

	protected Method getReflectMethod(SourceMethodBinding srcBinding) {
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
		if (params == null) {
			return new Type[0];
		}
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
