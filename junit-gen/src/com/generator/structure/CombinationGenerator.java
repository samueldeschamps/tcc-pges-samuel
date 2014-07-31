package com.generator.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationGenerator<E> {

	private final List<List<E>> categories;
	private List<List<E>> combinations;
	
	public static <E> List<List<E>> combine(List<List<E>> categories) {
		return new CombinationGenerator<>(categories).getCombinations();
	}

	public CombinationGenerator(List<List<E>> categories) {
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

	public void addCombinations(List<E> parentCombination, int idx) {
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

	public List<E> listFork(List<E> list, E newElement) {
		ArrayList<E> result = new ArrayList<>(list);
		result.add(newElement);
		return result;
	}

	// TODO Transformar isso aqui em uma classe de testes depois:
	public static void main(String[] args) {

		List<List<Object>> tipos = new ArrayList<>();
		tipos.add(Arrays.asList(new Object[] { "-1", "+1" }));
		tipos.add(Arrays.asList(new Object[] { "A", "B", "C" }));
		tipos.add(Arrays.asList(new Object[] { "x", "y", "z" }));
		tipos.add(Arrays.asList(new Object[] { "10", "20", "30", "40", "50" }));

		for (List<Object> list : new CombinationGenerator<Object>(tipos).getCombinations()) {
			System.out.println(list);
		}
	}


}
