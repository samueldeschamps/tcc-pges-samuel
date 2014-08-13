package com.generator.core;

import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.PrimitiveType.Primitive;
import japa.parser.ast.type.Type;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JavaParserAdapter {

	public static Type[] javaTypesToParserTypes(Class<?>[] types) {
		Type[] res = new Type[types.length];
		for (int i = 0; i < types.length; ++i) {
			res[i] = javaTypeToParserType(types[i]);
		}
		return res;
	}
	
	public static Type javaTypeToParserType(Class<?> type) {
		// Register types using a Map
		if (type.equals(boolean.class)) {
			return new PrimitiveType(Primitive.Boolean);
		}
		if (type.equals(byte.class)) {
			return new PrimitiveType(Primitive.Byte);
		}
		if (type.equals(short.class)) {
			return new PrimitiveType(Primitive.Short);
		}
		if (type.equals(char.class)) {
			return new PrimitiveType(Primitive.Char);
		}
		if (type.equals(int.class)) {
			return new PrimitiveType(Primitive.Int);
		}
		if (type.equals(long.class)) {
			return new PrimitiveType(Primitive.Long);
		}
		if (type.equals(float.class)) {
			return new PrimitiveType(Primitive.Float);
		}
		if (type.equals(double.class)) {
			return new PrimitiveType(Primitive.Double);
		}
		if (!type.isPrimitive() && !type.isArray() && !type.isAnnotation() && !type.isEnum()) {
			return new ClassOrInterfaceType(type.getSimpleName());
		}
		if (type.isArray()) {
			// FIXME Is this correct?
			return new ClassOrInterfaceType(type.getComponentType().getSimpleName() + "[]");
		}
		throw new IllegalArgumentException("Type not supported: '" + type + "'.");
		// TODO Suportar outros tipos
	}

	// TODO Create a registry to solve this many IFs:
	@SuppressWarnings("rawtypes")
	public static Expression valueToExpression(Object value) {
		if (value == null) {
			return new NullLiteralExpr();
		}
		if (value instanceof Boolean) {
			return new BooleanLiteralExpr((Boolean) value);
		}
		if (value instanceof Byte) {
			IntegerLiteralExpr literal = new IntegerLiteralExpr(value.toString());
			return new CastExpr(new PrimitiveType(Primitive.Byte), literal);
		}
		if (value instanceof Short) {
			IntegerLiteralExpr literal = new IntegerLiteralExpr(value.toString());
			return new CastExpr(new PrimitiveType(Primitive.Short), literal);
		}
		if (value instanceof Character) {
			return new CharLiteralExpr(value.toString());
		}
		if (value instanceof Integer) {
			return new IntegerLiteralExpr(value.toString());
		}
		if (value instanceof Long) {
			return new LongLiteralExpr(value.toString());
		}
		if (value instanceof Float) {
			DoubleLiteralExpr literal = new DoubleLiteralExpr(value.toString());
			return new CastExpr(new PrimitiveType(Primitive.Float), literal);
		}
		if (value instanceof Double) {
			return new DoubleLiteralExpr(value.toString());
		}
		if (value instanceof String) {
			return new StringLiteralExpr((String) value);
		}
		Class<? extends Object> clazz = value.getClass();
		if (clazz.isEnum()) {
			NameExpr scope = new NameExpr(clazz.getSimpleName());
			return new QualifiedNameExpr(scope, ((Enum) value).name());
		}
		if (clazz.isArray()) {
			// TODO Support array of array:
			Type compType = javaTypeToParserType(clazz.getComponentType());
			int length = Array.getLength(value);
			ArrayInitializerExpr initializer = new ArrayInitializerExpr(arrayValueToExpression(value, length));
			return new ArrayCreationExpr(compType, 1, initializer);
		}
		if (value instanceof BigDecimal) {
			BigDecimal decimal = (BigDecimal) value;
			Expression argument = new StringLiteralExpr(decimal.toPlainString());
			return new ObjectCreationExpr((ClassOrInterfaceType) javaTypeToParserType(BigDecimal.class), argument);
		}
		// TODO Suportar outros tipos
		throw new IllegalArgumentException("Value not supported: '" + value + "'. (" + clazz.getSimpleName() + ").");
	}

	private static List<Expression> arrayValueToExpression(Object array, int length) {
		List<Expression> res = new ArrayList<>();
		for (int i = 0; i < length; ++i) {
			res.add(valueToExpression(Array.get(array, i)));
		}
		return res;
	}

}
