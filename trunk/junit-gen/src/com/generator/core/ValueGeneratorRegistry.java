package com.generator.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.generator.core.util.Log;
import com.generator.core.valuegenerators.NotNullValues;
import com.generator.core.valuegenerators.SerialValueGeneratorComposite;
import com.generator.core.valuegenerators.random.RandomArrayValuesGenerator;

public class ValueGeneratorRegistry {

	private Map<Class<?>, List<Class<ValueGenerator<?>>>> generators = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T, VG extends ValueGenerator<T>> void register(Class<T> clazz, Class<VG> generator) {
		List<Class<ValueGenerator<?>>> innerList = generators.get(clazz);
		if (innerList == null) {
			innerList = new ArrayList<>();
			generators.put(clazz, innerList);
		}
		innerList.add((Class<ValueGenerator<?>>) generator);
	}

	@SuppressWarnings("rawtypes")
	private <T, VG extends ValueGenerator<T>> ValueGenerator<T> createInstance(Class<VG> generator, Class<?> valueClass) {
		try {
			return generator.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			try {
				Class<? extends Class> paramType = valueClass.getClass();
				Constructor<VG> constructor = generator.getConstructor(paramType);
				return constructor.newInstance(valueClass);
			} catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e1) {
				Log.error("Could not find a way to create a '" + generator.getSimpleName()
						+ "' object.\nValueGenerators must have a public no-argument constructor, "
						+ "or a public one-argument constructor with generators target type.");
				return null;
			}
		}
	}

	public <T> List<ValueGenerator<T>> get(Class<T> clazz, ValueGenerationStrategy strategy) {
		List<ValueGenerator<T>> result = get(clazz, clazz);
		for (Iterator<ValueGenerator<T>> it = result.iterator(); it.hasNext();) {
			if (it.next().getStrategy() != strategy) {
				it.remove();
			}
		}
		return result;
	}

	public <T> List<ValueGenerator<T>> get(Class<T> clazz) {
		return get(clazz, clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> List<ValueGenerator<T>> get(Class<T> registeredClass, Class<?> paramType) {
		List<ValueGenerator<T>> result = new ArrayList<>();
		if (registeredClass.isArray()) {
			// TODO Support other kinds of Array Generators (serial?).
			result.add((ValueGenerator<T>) new RandomArrayValuesGenerator<>(this, registeredClass));
		} else {
			List<Class<ValueGenerator<?>>> innerList = generators.get(registeredClass);
			if (innerList != null) {
				for (Class<ValueGenerator<?>> generator : innerList) {
					Class<?> damnedGenerics = generator;
					ValueGenerator<T> instance = this.<T, ValueGenerator<T>> createInstance(
							(Class<ValueGenerator<T>>) damnedGenerics, paramType);
					if (instance != null) {
						result.add(instance);
					}
				}
			}
			// Enumeration types have generic value generators:
			if (registeredClass.isEnum() && !registeredClass.equals(Enum.class)) {
				result.addAll((Collection<? extends ValueGenerator<T>>) get(Enum.class, paramType));
			}
		}
		return result;
	}

	public <T> ValueGenerator<T> getComposite(Class<T> paramType) {
		List<ValueGenerator<T>> list = get(paramType);
		ValueGenerator<T> result = new SerialValueGeneratorComposite<T>(list);
		if (paramType.isPrimitive()) {
			result = new NotNullValues<T>(result);
		}
		return result;
	}

}
