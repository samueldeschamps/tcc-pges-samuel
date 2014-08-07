package com.generator.structure;

import java.util.ArrayList;
import java.util.List;

// TODO Is there a way to optimize performance / memory consumption of this guy?
public class Combinator<E> {

	private final List<List<E>> categories;
	private List<List<E>> combinations;

	public static <E> List<List<E>> combine(List<List<E>> categories) {
		return new Combinator<>(categories).getCombinations();
	}

	public Combinator(List<List<E>> categories) {
		this.categories = categories;
	}

	public List<List<E>> getCombinations() {
		if (combinations == null) {
			combinations = new ArrayList<List<E>>();
			if (!categories.isEmpty()) {
				addCombinations(new ArrayList<E>(), 0);
			}
		}
		return combinations;
	}

	private void addCombinations(List<E> parentCombination, int idx) {
		List<E> categElements = categories.get(idx);
		if (idx < categories.size() - 1) {
			for (int i = 0; i < categElements.size(); ++i) {
				addCombinations(listFork(parentCombination, categElements.get(i)), idx + 1);
			}
		} else {
			// It's the last category
			for (int i = 0; i < categElements.size(); ++i) {
				combinations.add(listFork(parentCombination, categElements.get(i)));
			}
		}
	}

	private List<E> listFork(List<E> list, E newElement) {
		ArrayList<E> result = new ArrayList<>(list);
		result.add(newElement);
		return result;
	}

}
