package com.generator.core;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.AssignExpr.Operator;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.type.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.generator.core.util.Log;
import com.generator.core.util.Util;

public class CodeGenerator {

	private final JUnitGenerator generator;
	private Class<?> targetClass;
	private Map<Method, List<TestCaseData>> cases;
	private String testPackageName;
	private CompilationUnit unit;
	private Map<String, Integer> overloadCount = new HashMap<>();

	public CodeGenerator(JUnitGenerator generator) {
		this.generator = generator;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Map<Method, List<TestCaseData>> getCases() {
		return cases;
	}

	public void setCases(Map<Method, List<TestCaseData>> cases) {
		this.cases = cases;
	}

	public String getTestPackageName() {
		return testPackageName;
	}

	public void setTestPackageName(String testPackageName) {
		this.testPackageName = testPackageName;
	}

	public void execute() {

		if (testPackageName == null || testPackageName.isEmpty()) {
			Log.warning("Test package name not informed. Assuming target class package.");
			testPackageName = targetClass.getPackage().getName();
		}

		unit = new CompilationUnit();
		unit.setPackage(new PackageDeclaration(ASTHelper.createNameExpr(testPackageName)));
		String testClassName = targetClass.getSimpleName() + "Test";

		ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, testClassName);
		ASTHelper.addTypeDeclaration(unit, type);

		addDefaultImports(unit, targetClass, testPackageName);

		for (Entry<Method, List<TestCaseData>> entry : cases.entrySet()) {
			Method method = entry.getKey();
			List<TestCaseData> methodCases = entry.getValue();

			if ((method.getModifiers() & Modifier.STATIC) == 0) {
				// TODO Suportar métodos não estáticos.
				continue;
			}

			Integer overCount = overloadCount.get(method.getName());
			if (overCount == null) {
				overCount = 0;
			}
			overloadCount.put(method.getName(), ++overCount);
			int counter = 1;
			for (TestCaseData caseData : methodCases) {
				String uniqueName = Util.capitalize(method.getName());
				if (overCount > 1) {
					uniqueName = uniqueName + overCount;
				}
				String testMethodName = "test" + uniqueName + "_" + counter++;
				MethodDeclaration testMethod = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE,
						testMethodName);
				testMethod.addAnnotation(new MarkerAnnotationExpr("Test"));
				ASTHelper.addMember(type, testMethod);

				BlockStmt block = new BlockStmt();
				testMethod.setBody(block);

				NameExpr clazzExpr = new NameExpr(targetClass.getSimpleName());
				MethodCallExpr call = new MethodCallExpr(clazzExpr, method.getName());
				for (Object paramValue : caseData.getParamValues()) {
					ASTHelper.addArgument(call, JavaParserAdapter.valueToExpression(paramValue));
					if (paramValue != null) {
						addImportIfNecessary(paramValue.getClass());
					}
				}

				ExecutionResult result = caseData.getResult();
				if (result.succeeded()) {
					addResultAssertion(method, block, call, result);
					addThrowsClauseIfNeeded(method, testMethod);
				} else if (result.isInfiniteLoop()) {
					ASTHelper.addStmt(block, call);
					String text = String.format("FIXME: Infinite loop detected! (Took more than %d seconds)",
							generator.getInfiniteLoopTimeout());
					testMethod.setJavaDoc(new JavadocComment(formatJavadoc(text)));
				} else {
					boolean whenDeclared = generator.getExceptionsStrategy() == ExceptionsStrategy.ASSERT_WHEN_DECLARED;
					if (whenDeclared && !result.isExceptionDeclared()) {
						// Add only a method call to force throw the exception:
						ASTHelper.addStmt(block, call);
					} else {
						addExceptionAssertion(block, call, result);
					}
				}
				if (!result.isInfiniteLoop()) {
					String text = String.format("Coverage: %.2f%%", result.getShallowCoverageRatio() * 100);
					testMethod.setJavaDoc(new JavadocComment(formatJavadoc(text)));
				}
			}
		}
	}

	private void addThrowsClauseIfNeeded(Method method, MethodDeclaration testMethod) {
		Class<?>[] exceptions = method.getExceptionTypes();
		if (exceptions == null) {
			return;
		}
		for (Class<?> exceptionClass : exceptions) {
			// Ignore if this exception is unchecked
			if (RuntimeException.class.isAssignableFrom(exceptionClass)) {
				continue;
			}
			if (Error.class.isAssignableFrom(exceptionClass)) {
				continue;
			}
			testMethod.addThrows(new NameExpr(exceptionClass.getSimpleName()));
			addImportIfNecessary(exceptionClass);
		}
	}

	// TODO Do the identing in JavaParser dump methods.
	private String formatJavadoc(String text) {
		return "\n     * " + text.replace("\n", "\n    * ") + "\n     ";
	}

	public CompilationUnit getResult() {
		return unit;
	}

	private void addDefaultImports(CompilationUnit unit, Class<?> targetClass, String testPackageName) {

		unit.addImport(new ImportDeclaration("org.junit.Assert", false, false));
		unit.addImport(new ImportDeclaration("org.junit.Test", false, false));

		if (!testPackageName.equals(targetClass.getPackage().getName())) {
			unit.addImport(new ImportDeclaration(targetClass.getName(), false, false));
		}
	}

	private void addResultAssertion(Method method, BlockStmt block, MethodCallExpr call, ExecutionResult execResult) {
		String actualName = "actual";
		Type type = JavaParserAdapter.javaTypeToParserType(method.getReturnType());
		addImportIfNecessary(method.getReturnType());
		VariableDeclarationExpr varDecl = ASTHelper.createVariableDeclarationExpr(type, actualName);
		AssignExpr assign = new AssignExpr(varDecl, call, Operator.assign);
		ASTHelper.addStmt(block, assign);

		NameExpr assertName = new NameExpr("Assert");
		MethodCallExpr assertCall;
		if (method.getReturnType().isArray()) {
			assertCall = new MethodCallExpr(assertName, "assertArrayEquals");
		} else {
			assertCall = new MethodCallExpr(assertName, "assertEquals");
		}
		Object resultValue = execResult.getResult();
		Expression resulExpression = JavaParserAdapter.valueToExpression(resultValue);
		if (resultValue != null) {
			addImportIfNecessary(resultValue.getClass());
		}
		Expression expectedExpr;
		if (resultIsBig(resultValue)) {
			String expectedName = "expected";
			VariableDeclarationExpr expectVarDecl = ASTHelper.createVariableDeclarationExpr(type, expectedName);
			AssignExpr expectAssign = new AssignExpr(expectVarDecl, resulExpression, Operator.assign);
			ASTHelper.addStmt(block, expectAssign);
			expectedExpr = new NameExpr(expectedName);
		} else {
			expectedExpr = resulExpression;
		}
		ASTHelper.addArgument(assertCall, expectedExpr);
		ASTHelper.addArgument(assertCall, new NameExpr(actualName));
		if (resultValue instanceof Double || resultValue instanceof Float) {
			String precision = String.valueOf(generator.getDoubleAssertPrecision());
			ASTHelper.addArgument(assertCall, new DoubleLiteralExpr(precision));
		}
		ASTHelper.addStmt(block, assertCall);
	}

	private void addImportIfNecessary(Class<?> type) {
		if (type.isArray()) {
			addImportIfNecessary((type.getComponentType()));
			return;
		}
		if (type.isEnum()) {
			addImport(type);
			return;
		}
		if (!type.isPrimitive() && !type.isArray() && !type.isAnnotation() && !type.isEnum()) {
			addImport(type);
			return;
		}
	}

	private boolean resultIsBig(Object resultValue) {
		if (resultValue == null) {
			return false;
		}
		if (resultValue.getClass().isArray()) {
			return true;
		}
		// TODO Add more cases.
		return false;
	}

	private void addExceptionAssertion(BlockStmt block, MethodCallExpr call, ExecutionResult result) {
		BlockStmt tryBlock = new BlockStmt();
		ASTHelper.addStmt(tryBlock, call);
		NameExpr assertName = new NameExpr("Assert");
		MethodCallExpr failCall = new MethodCallExpr(assertName, "fail");
		ASTHelper.addArgument(failCall, new StringLiteralExpr(getFailMessage(result)));
		ASTHelper.addStmt(tryBlock, failCall);

		CatchClause catchClause = new CatchClause();
		Type exceptionType = JavaParserAdapter.javaTypeToParserType(result.getExceptionClass());
		addImportIfNecessary(result.getExceptionClass());
		String exceptionVarName = "ex";
		catchClause.setExcept(new Parameter(exceptionType, new VariableDeclaratorId(exceptionVarName)));

		BlockStmt catchBlock = new BlockStmt();
		if (result.getException().getMessage() != null) {
			NameExpr assertName2 = new NameExpr("Assert");
			MethodCallExpr assertCall = new MethodCallExpr(assertName2, "assertEquals");
			ASTHelper.addArgument(assertCall, new StringLiteralExpr(result.getException().getMessage()));
			MethodCallExpr callGetMessage = new MethodCallExpr(new NameExpr(exceptionVarName), "getMessage");
			ASTHelper.addArgument(assertCall, callGetMessage);
			ASTHelper.addStmt(catchBlock, assertCall);
		}
		catchClause.setCatchBlock(catchBlock);
		ArrayList<CatchClause> catchs = new ArrayList<CatchClause>();
		catchs.add(catchClause);
		TryStmt tryStmt = new TryStmt(tryBlock, catchs, null);
		ASTHelper.addStmt(block, tryStmt);
	}

	private String getFailMessage(ExecutionResult result) {
		String exceptionName = result.getExceptionClass().getSimpleName();
		return "A" + (Util.startsWithVowel(exceptionName) ? "n " : " ") + exceptionName + " must have been thrown.";
	}

	private void addImport(Class<?> type) {
		String className = type.getName();
		String pkgName = type.getPackage().getName();
		// Inner class treatment:
		if (type.getEnclosingClass() != null) {
			className = className.replace("$", ".");
			pkgName = type.getEnclosingClass().getName();
		}
		if (pkgName.equals("java.lang") || pkgName.equals(targetClass.getPackage().getName())) {
			// This class doesn't need import
			return;
		}
		for (ImportDeclaration imp : unit.getImports()) {
			if (imp.getName().getName().equals(className) && !imp.isAsterisk() && !imp.isStatic()) {
				// Import already included
				return;
			}
		}
		unit.addImport(new ImportDeclaration(className, false, false));
	}

}
