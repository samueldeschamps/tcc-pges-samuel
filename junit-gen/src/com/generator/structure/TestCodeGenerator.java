package com.generator.structure;

import japa.parser.ASTHelper;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.AssignExpr.Operator;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.PrimitiveType.Primitive;
import japa.parser.ast.type.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestCodeGenerator {

	private Class<?> targetClass;
	private Map<Method, List<TestCaseData>> cases;
	private String testPackageName;
	private CompilationUnit unit;

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
			int counter = 1;
			for (TestCaseData caseData : methodCases) {
				String testMethodName = "test" + method.getName() + "_" + counter++;
				MethodDeclaration testMethod = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE,
						testMethodName);
				testMethod.addAnnotation(new MarkerAnnotationExpr("Test"));
				ASTHelper.addMember(type, testMethod);

				BlockStmt block = new BlockStmt();
				testMethod.setBody(block);

				NameExpr clazzExpr = new NameExpr(targetClass.getSimpleName());
				MethodCallExpr call = new MethodCallExpr(clazzExpr, method.getName());
				for (Object paramValue : caseData.getParamValues()) {
					ASTHelper.addArgument(call, valueToExpression(paramValue));
				}

				ExecutionResult result = caseData.getResult();
				if (result.succeeded()) {
					addResultAssertion(method, block, call, result);
				} else {
					addExceptionAssertion(block, call, result);
				}
			}
		}
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

	private void addResultAssertion(Method method, BlockStmt block, MethodCallExpr call, ExecutionResult result) {
		String actualName = "actual";
		Type type = javaTypeToParserType(method.getReturnType());
		VariableDeclarationExpr varDecl = ASTHelper.createVariableDeclarationExpr(type, actualName);
		AssignExpr assign = new AssignExpr(varDecl, call, Operator.assign);
		ASTHelper.addStmt(block, assign);

		NameExpr assertName = new NameExpr("Assert");
		MethodCallExpr assertCall = new MethodCallExpr(assertName, "assertEquals");
		ASTHelper.addArgument(assertCall, valueToExpression(result.result));
		ASTHelper.addArgument(assertCall, new NameExpr(actualName));
		if (result.result instanceof Double) {
			// TODO Parametrizar a precisão do double
			ASTHelper.addArgument(assertCall, new DoubleLiteralExpr("0.00000001"));
		}
		ASTHelper.addStmt(block, assertCall);
	}

	private void addExceptionAssertion(BlockStmt block, MethodCallExpr call, ExecutionResult result) {
		BlockStmt tryBlock = new BlockStmt();
		ASTHelper.addStmt(tryBlock, call);
		NameExpr assertName = new NameExpr("Assert");
		MethodCallExpr failCall = new MethodCallExpr(assertName, "fail");
		ASTHelper.addArgument(failCall, new StringLiteralExpr(getFailMessage(result)));
		ASTHelper.addStmt(tryBlock, failCall);

		CatchClause catchClause = new CatchClause();
		Type exceptionType = javaTypeToParserType(result.getExceptionClass());
		String exceptionVarName = "ex";
		catchClause.setExcept(new Parameter(exceptionType, new VariableDeclaratorId(exceptionVarName)));

		BlockStmt catchBlock = new BlockStmt();
		if (result.exception.getMessage() != null) {
			NameExpr assertName2 = new NameExpr("Assert");
			MethodCallExpr assertCall = new MethodCallExpr(assertName2, "assertEquals");
			ASTHelper.addArgument(assertCall, new StringLiteralExpr(result.exception.getMessage()));
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

	private Type javaTypeToParserType(Class<?> type) {
		if (type.equals(int.class)) {
			return new PrimitiveType(Primitive.Int);
		}
		if (type.equals(boolean.class)) {
			return new PrimitiveType(Primitive.Boolean);
		}
		if (type.equals(double.class)) {
			return new PrimitiveType(Primitive.Double);
		}
		if (!type.isPrimitive() && !type.isArray() && !type.isAnnotation() && !type.isEnum()) {
			String pkgName = type.getPackage().getName();
			if (!pkgName.equals("java.lang") && !pkgName.equals(targetClass.getPackage().getName())) {
				addImport(type);
			}
			return new ClassOrInterfaceType(type.getSimpleName());
		}
		throw new IllegalArgumentException("Type not supported: '" + type + "'.");
		// TODO Suportar outros tipos
	}

	private void addImport(Class<?> type) {
		String className = type.getName();
		for (ImportDeclaration imp : unit.getImports()) {
			if (imp.getName().getName().equals(className) && !imp.isAsterisk() && !imp.isStatic()) {
				// Import already included
				return;
			}
		}
		unit.addImport(new ImportDeclaration(className, false, false));
	}

	private Expression valueToExpression(Object value) {
		if (value == null) {
			return new NullLiteralExpr();
		}
		if (value instanceof String) {
			return new StringLiteralExpr((String) value);
		}
		if (value instanceof Boolean) {
			return new BooleanLiteralExpr((Boolean) value);
		}
		if (value instanceof Integer) {
			return new IntegerLiteralExpr(value.toString());
		}
		if (value instanceof Double) {
			return new DoubleLiteralExpr(value.toString());
		}
		// TODO Suportar outros tipos
		throw new IllegalArgumentException("Value not supported: '" + value + "'. (" + value.getClass().getSimpleName() + ").");
	}

}
