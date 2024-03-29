package com.generator.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.generator.core.valuegenerators.UniqueValues;

public class ValueSetGenerator {

	private List<UniqueValues<?>> generators = new ArrayList<>();

	private int index = -1;
	private ValueGenerator<?> current;
	private LinkedList<List<Object>> buffer = new LinkedList<>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ValueSetGenerator(ValueGenerator<?>[] valueGenerators) {
		for (ValueGenerator<?> gen : valueGenerators) {
			generators.add(new UniqueValues(gen));
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
		for (UniqueValues<?> gen : generators) {
			if (!gen.hasNext()) {
				return false;
			}
		}
		List<Object> firstSet = new ArrayList<>();
		for (UniqueValues<?> gen : generators) {
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
		for (UniqueValues<?> item : generators) {
			List<Object> history = new ArrayList<>(item.getHistory());
			if (item != current) {
				categories.add(history);
			} else {
				List<Object> categ = new ArrayList<>();
				categ.add(history.get(history.size() - 1));
				categories.add(categ);
			}
		}
		List<List<Object>> combinations = Combinator.combine(categories);
		buffer.addAll(combinations);
	}

}
