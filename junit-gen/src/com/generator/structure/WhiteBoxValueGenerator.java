package com.generator.structure;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;

public abstract class WhiteBoxValueGenerator<T> implements ValueGenerator<T> {

	protected MethodDeclaration methodDecl;
	protected Parameter param;

	public WhiteBoxValueGenerator(MethodDeclaration methodDecl, Parameter param) {
		this.methodDecl = methodDecl;
		this.param = param;
	}

}
