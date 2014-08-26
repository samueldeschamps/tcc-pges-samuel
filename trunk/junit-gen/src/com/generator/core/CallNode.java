package com.generator.core;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class CallNode {

	private final Method method;
	private final CallNode parent;
	private List<CallNode> calleds;

	public CallNode(Method method) {
		this(method, null);
	}

	public CallNode(Method method, CallNode parent) {
		this.method = method;
		this.parent = parent;
	}

	public void addCalled(CallNode called) {
		if (calleds == null) {
			calleds = new LinkedList<>();
		}
		calleds.add(called);
	}

	public Method getMethod() {
		return method;
	}

	public CallNode getParent() {
		return parent;
	}

	public List<CallNode> getCalleds() {
		return calleds;
	}
	
	public boolean isAncestor(Method method) {
		if (method.equals(this.method)) {
			return true;
		}
		if (parent != null) {
			return parent.isAncestor(method);
		} else {
			return false;
		}
	}

	public boolean hasChildren() {
		return calleds != null && !calleds.isEmpty();
	} 

}
