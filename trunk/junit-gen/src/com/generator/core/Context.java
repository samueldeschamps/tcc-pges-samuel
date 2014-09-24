package com.generator.core;

import java.lang.reflect.Method;

public class Context {

	private static Context instance = null;

	private Method currMethod;
	private int currParamIdx;

	public static synchronized Context get() {
		if (instance == null) {
			instance = new Context();
		}
		return instance;
	}

	private Context() {
	}

	public Method getCurrMethod() {
		return currMethod;
	}

	public void setCurrMethod(Method currMethod) {
		this.currMethod = currMethod;
	}

	public int getCurrParamIdx() {
		return currParamIdx;
	}

	public void setCurrParamIdx(int currParamIdx) {
		this.currParamIdx = currParamIdx;
	}

}
