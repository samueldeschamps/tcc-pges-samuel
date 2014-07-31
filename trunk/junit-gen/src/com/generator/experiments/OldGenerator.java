package com.generator.experiments;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.AssignExpr.Operator;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.PrimitiveType.Primitive;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class OldGenerator {

	public static void main(String[] args) throws ReflectiveOperationException {
		generateCalculatorTests();
	}

	private static void generateCalculatorTests() throws ClassNotFoundException, ReflectiveOperationException {
		String className = "com.target.Calculator";

		Class<?> clazz = Class.forName(className);
		String testClassName = clazz.getSimpleName() + "Test";
		String testPkgName = clazz.getPackage().getName();
		String testMethodPrefix = "test_";

		CompilationUnit unit = generateTestUnit(className, clazz, testClassName, testPkgName, testMethodPrefix);
		System.out.println(unit);
	}

	private static CompilationUnit generateTestUnit(String className, Class<?> clazz, String testClassName,
			String testPkgName, String testMethodPrefix) throws ReflectiveOperationException {

		CompilationUnit unit = new CompilationUnit();
		unit.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(testPkgName)));
		ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, testClassName);
		ASTHelper.addTypeDeclaration(unit, type);

		unit.addImport(new ImportDeclaration(new NameExpr("org.junit.Assert"), false, false));
		unit.addImport(new ImportDeclaration(new NameExpr("org.junit.Test"), false, false));

		for (Method method : clazz.getDeclaredMethods()) {

			if ((method.getModifiers() & Modifier.STATIC) == 0) {
				// TODO Suportar métodos não estáticos.
				continue;
			}

			ParamValueGenerator paramValGen = new BoundaryValuesGenerator(method);
			int counter = 1;
			while (paramValGen.hasNext()) {
				String testMethodName = testMethodPrefix + method.getName() + "_" + counter++;
				MethodDeclaration testMethod = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE,
						testMethodName);
				testMethod.addAnnotation(new MarkerAnnotationExpr("Test"));
				ASTHelper.addMember(type, testMethod);

				DynamicExecutor executor = new DynamicExecutor();
				executor.setClassName(className);
				executor.setMethodName(method.getName());
				List<Object> paramSet = paramValGen.nextSet();
				InvocationResult result = executor.getResultFromParams(paramSet);

				BlockStmt block = new BlockStmt();
				testMethod.setBody(block);

				// add a statement do the method body
				NameExpr clazzExpr = new NameExpr(clazz.getSimpleName());
				MethodCallExpr call = new MethodCallExpr(clazzExpr, method.getName());
				for (Object param : paramSet) {
					// TODO Suportar outros tipos
					ASTHelper.addArgument(call, new IntegerLiteralExpr(param.toString()));
				}
				String actualName = "actual";
				VariableDeclarationExpr varDecl = ASTHelper.createVariableDeclarationExpr(new PrimitiveType(
						Primitive.Int), actualName);
				AssignExpr assign = new AssignExpr(varDecl, call, Operator.assign);
				ASTHelper.addStmt(block, assign);

				if (result.succeeded()) {
					NameExpr assertName = new NameExpr("Assert");
					MethodCallExpr assertCall = new MethodCallExpr(assertName, "assertEquals");
					ASTHelper.addArgument(assertCall, new IntegerLiteralExpr(result.result.toString()));
					ASTHelper.addArgument(assertCall, new NameExpr(actualName));
					ASTHelper.addStmt(block, assertCall);
				} else {
					// TODO
				}
			}

		}
		return unit;
	}

}
