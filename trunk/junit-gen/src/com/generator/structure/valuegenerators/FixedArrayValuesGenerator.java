package com.generator.structure.valuegenerators;

import java.lang.reflect.Array;
import java.util.List;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.ValueGenerator;
import com.generator.core.ValueGeneratorRegistry;
import com.generator.core.ValueSetGenerator;
import com.generator.structure.util.Util;

public class FixedArrayValuesGenerator<T> implements ValueGenerator<T[]> {

	private final ValueSetGenerator inner;
	private final Class<?> componentType;

	// TODO Remove dependency from Registry.
	@SuppressWarnings("rawtypes")
	public FixedArrayValuesGenerator(ValueGeneratorRegistry registry, Class<?> arrayType, int arrayLength) {
		if (!arrayType.isArray()) {
			throw new IllegalArgumentException("ArrayType must be an array!");
		}
		Class<?> compType = arrayType.getComponentType();
		if (compType.isPrimitive()) {
			compType = Util.primitiveToWrapper(compType);
		}
		ValueGenerator[] generators = new ValueGenerator[arrayLength];
		for (int i = 0; i < arrayLength; ++i) {
			generators[i] = registry.getComposite(compType);
		}
		this.componentType = compType;
		this.inner = new ValueSetGenerator(generators);
	}

	@Override
	public boolean hasNext() {
		return inner.hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] next() {
		List<Object> next = inner.next();
		T[] res = (T[]) Array.newInstance(componentType, next.size());
		// TODO Improve performance!!!
		for (int i = 0; i < next.size(); ++i) {
			res[i] = (T) next.get(i);
		}
		return res;
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.BRUTE_FORCE;
	}

	// public static void main(String[] args) {
	//
	// ValueGeneratorRegistry registry = new ValueGeneratorRegistry();
	// registry.register(int.class, IntegerCommonValues.class);
	// FixedArrayValuesGenerator<Integer> gen = new
	// FixedArrayValuesGenerator<Integer>(registry, int[].class, 2);
	// while (gen.hasNext()) {
	// System.out.println(Arrays.toString(gen.next()));
	// }
	// }

}
