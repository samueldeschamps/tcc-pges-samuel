package com.generator.structure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.generator.structure.valuegenerators.common.BooleanFullValues;
import com.generator.structure.valuegenerators.common.DoubleCommonValues;
import com.generator.structure.valuegenerators.common.IntegerCommonValues;
import com.generator.structure.valuegenerators.common.StringCommonValues;

public class ValueSetGenerator {

	private List<Item> generators = new ArrayList<>();

	private int index = -1;
	private Item current;
	private LinkedList<List<Object>> buffer = new LinkedList<>();

	public ValueSetGenerator(ValueGeneratorRegistry registry, Class<?>[] paramTypes) {
		for (Class<?> paramType : paramTypes) {
			List<?> list = registry.get(paramType);
			ValueGeneratorComposite generator = new ValueGeneratorComposite((List<ValueGenerator<?>>) list);
			Item item;
			if (paramType.isPrimitive()) {
				item = new PrimitiveItem(generator);
			} else {
				item = new Item(generator);
			}
			generators.add(item);
		}
	}

	public boolean hasNext() {
		if (!buffer.isEmpty()) {
			return true;
		}
		if (current == null) {
			return init();
		}
		return moveForward();
	}

	public List<Object> next() {
		return buffer.removeFirst();
	}

	private boolean moveForward() {
		int missCount = 0;
		for (;;) {
			++index;
			if (index >= generators.size()) {
				index = 0;
			}
			current = generators.get(index);
			if (current.hasNext()) {
				// Call next to add one more to the item history:
				current.next();
				combine();
				return true;
			} else {
				++missCount;
				if (missCount == generators.size()) {
					return false;
				}
			}
		}
	}

	private boolean init() {
		if (generators.size() == 0) {
			return false;
		}
		for (Item gen : generators) {
			if (!gen.hasNext()) {
				return false;
			}
		}
		List<Object> firstSet = new ArrayList<>();
		for (Item gen : generators) {
			firstSet.add(gen.next());
		}
		buffer.add(firstSet);
		current = generators.get(0);
		return true;
	}

	private void combine() {
		List<List<Object>> categories = new ArrayList<>();
		// Gets all generated elements from the generators, except the current
		// (from which gets only the last one):
		for (Item item : generators) {
			List<Object> history = item.getHistory();
			if (item != current) {
				categories.add(history);
			} else {
				List<Object> categ = new ArrayList<>();
				categ.add(history.get(history.size() - 1));
				categories.add(categ);
			}
		}
		List<List<Object>> combinations = CombinationGenerator.combine(categories);
		buffer.addAll(combinations);
	}

	private static class Item {

		protected final ValueGenerator<?> generator;
		protected final List<Object> history = new ArrayList<>();

		public Item(ValueGenerator<?> generator) {
			this.generator = generator;
		}

		public boolean hasNext() {
			return generator.hasNext();
		}

		public Object next() {
			Object next = generator.next();
			history.add(next);
			return next;
		}

		public List<Object> getHistory() {
			return history;
		}
	}

	/**
	 * Always "jumps" null values (incompatible with primitives).
	 */
	private static class PrimitiveItem extends Item {

		private Object next;

		public PrimitiveItem(ValueGenerator<?> generator) {
			super(generator);
		}

		public boolean hasNext() {
			for (;;) {
				if (!generator.hasNext()) {
					next = null;
					return false;
				} else {
					next = generator.next();
					if (next != null) {
						return true;
					}
				}
			}
		}

		public Object next() {
			history.add(next);
			return next;
		}

		public List<Object> getHistory() {
			return history;
		}

	}

	public static void main(String[] args) throws InterruptedException {

		ValueGeneratorRegistry registry = new ValueGeneratorRegistry();
		registry.register(Integer.class, IntegerCommonValues.class);
		registry.register(int.class, IntegerCommonValues.class);
		// registry.register(Integer.class, IntegerRandomValues.class);
		registry.register(String.class, StringCommonValues.class);
		registry.register(Double.class, DoubleCommonValues.class);
		registry.register(Boolean.class, BooleanFullValues.class);
		registry.register(boolean.class, BooleanFullValues.class);

		// Class<?>[] paramTypes = new Class<?>[] { Integer.class, Integer.class
		// };
		Class<?>[] paramTypes = new Class<?>[] { int.class, boolean.class };
		ValueSetGenerator setGen = new ValueSetGenerator(registry, paramTypes);

		long before = System.currentTimeMillis();

		int count = 0;
		while (setGen.hasNext()) {
			count++;
			List<Object> next = setGen.next();
			System.out.println(next);
			// Thread.sleep(10L);
		}

		long after = System.currentTimeMillis();
		System.out.println("Count=" + count);
		System.out.println("Time=" + (after - before) / 1000.0);
	}

}
