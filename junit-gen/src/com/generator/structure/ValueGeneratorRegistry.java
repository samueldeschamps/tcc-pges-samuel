package com.generator.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValueGeneratorRegistry {

	// TODO Support many generators for the same class/strategy:
	private Map<Class<?>, Map<ValueGenerationStrategy, Class<ValueGenerator<?>>>> generators = new HashMap<>();

	public <T, VG extends ValueGenerator<T>> void register(Class<T> clazz, Class<VG> generator) {
		Map<ValueGenerationStrategy, Class<ValueGenerator<?>>> innerMap = generators.get(clazz);
		if (innerMap == null) {
			innerMap = new LinkedHashMap<>();
			generators.put(clazz, innerMap);
		}
		// FIXME Instanciar um objeto só pra isso ficou estranho.
		ValueGenerator<T> instance = createInstance(generator);
		innerMap.put(instance.getStrategy(), (Class<ValueGenerator<?>>) generator);
	}

	private <T, VG extends ValueGenerator<T>> ValueGenerator<T> createInstance(Class<VG> generator) {
		try {
			return generator.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			String msg = "Class %s must have a public, no-argument constructor.";
			throw new RuntimeException(String.format(msg, generator.getSimpleName()), e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> ValueGenerator<T> get(Class<T> clazz, ValueGenerationStrategy strategy) {
		Map<ValueGenerationStrategy, Class<ValueGenerator<?>>> innerMap = generators.get(clazz);
		if (innerMap == null) {
			return null;
		}
		Class<?> generator = innerMap.get(strategy);
		return createInstance((Class<ValueGenerator<T>>) generator);
	}

	@SuppressWarnings("unchecked")
	public <T> List<ValueGenerator<T>> get(Class<T> clazz) {
		Map<ValueGenerationStrategy, Class<ValueGenerator<?>>> innerMap = generators.get(clazz);
		if (innerMap == null) {
			return null;
		}
		ArrayList<ValueGenerator<T>> result = new ArrayList<ValueGenerator<T>>();
		for (Class<ValueGenerator<?>> generator : innerMap.values()) {
			Class<?> damnedGenerics = generator;
			result.add(createInstance((Class<ValueGenerator<T>>) damnedGenerics));
		}
		return result;
	}

	/*
	private static class GeneratorKey {

		final Class<?> type;
		final ValueGenerationStrategy strategy;

		public GeneratorKey(Class<?> type, ValueGenerationStrategy strategy) {
			this.type = type;
			this.strategy = strategy;
		}

		@Override
		public int hashCode() {
			return type.hashCode() * 7 + strategy.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof GeneratorKey)) {
				return false;
			}
			GeneratorKey other = (GeneratorKey) obj;
			return strategy == other.strategy && type.equals(other.type);
		}
	}
	*/

}
