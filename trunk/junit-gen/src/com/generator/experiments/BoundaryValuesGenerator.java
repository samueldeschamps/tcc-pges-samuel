package com.generator.experiments;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoundaryValuesGenerator extends ParamValueGenerator {

	private List<List<Object>> combinations;
	private int index = 0;

	public BoundaryValuesGenerator(Method method) {
		this.method = method;
		Class<?>[] types = method.getParameterTypes();

		List<List<Object>> boundaries = new ArrayList<>();
		for (Class<?> type : types) {
			boundaries.add(getBoundaryValues(type));
		}
		this.combinations = CombinationGenerator.combine(boundaries);
	}

	@Override
	public boolean hasNext() {
		return index < combinations.size();
	}

	@Override
	public List<Object> nextSet() {
		return combinations.get(index++);
	}

	public List<Object> getBoundaryValues(Class<?> type) {
		
		// TODO Transformar numa Factory
		// TODO Suportar mais tipos/valores
		
		if (type.equals(int.class)) {
			return Arrays.asList(new Object[] { -1000, -1, 0, 1, 1000 });
		}
		throw new UnsupportedOperationException(String.format("Type %s not supported.", type.toString()));
	}

}
