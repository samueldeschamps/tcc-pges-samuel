package com.generator.core.valuegenerators.symbolic;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import com.generator.core.CallNode;
import com.generator.core.CodeInfo;
import com.generator.core.Context;
import com.generator.core.ValueGenerationStrategy;
import com.generator.core.valuegenerators.CachedValueGenerator;
import com.generator.core.valuegenerators.MethodContextValueGenerator;

/**
 * Coleta as string literais contidas dentro do método alvo e também dos métodos
 * que são chamados pelo método alvo. (1 nível).
 */
public class StringLiteralsValueGenerator extends MethodContextValueGenerator<String> {

	private CachedValueGenerator<String> cache;

	public StringLiteralsValueGenerator() {
		super();
		Set<String> values = findValues();
		String[] array = values.toArray(new String[values.size()]);
		cache = new CachedValueGenerator<>(array);
	}

	private Set<String> findValues() {
		CodeInfo codeInfo = Context.get().getCodeInfo();
		Set<String> result = new LinkedHashSet<>();
		addLiterals(codeInfo, result, getMethod());
		CallNode calleds = codeInfo.getCallHierarchy(getMethod());
		if (calleds != null && calleds.getCalleds() != null) {
			for (CallNode called : calleds.getCalleds()) {
				addLiterals(codeInfo, result, called.getMethod());
			}
		}
		return result;
	}

	private void addLiterals(CodeInfo codeInfo, Set<String> result, Method method) {
		Set<String> literals = codeInfo.getStringLiterals(method);
		if (literals != null) {
			result.addAll(literals);
		}
	}

	@Override
	public boolean hasNext() {
		return cache.hasNext();
	}

	@Override
	public String next() {
		return cache.next();
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.DATA_FLOW_STRATEGY;
	}

}
