package com.generator.core.comparators;

import java.lang.reflect.Array;
import java.util.Comparator;

import com.generator.core.util.Log;

public class ComplexityComparator implements Comparator<Object> {

	private StringComplexityComparator strComparator = new StringComplexityComparator();

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 == null || o2 == null) {
			return Boolean.compare(o1 != null, o2 != null);
		}
		if (o1 instanceof Number && o1 instanceof Comparable) {
			// Prefers the closest to zero:
			double d1 = ((Number) o1).doubleValue();
			double d2 = ((Number) o2).doubleValue();
			int res = Double.compare(Math.abs(d1), Math.abs(d2));
			if (res != 0) {
				return res;
			}
			// If abs are equal, maybe one is negative and other is
			// positive. In this case, prefer the positive.
			return Double.compare(d2, d1);
		}
		if (o1 instanceof String) {
			return strComparator.compare((String) o1, (String) o2);
		}
		if (o1 instanceof Enum) {
			return 0;
		}
		if (o1 instanceof Boolean) {
			return 0;
		}
		if (o1 instanceof Character) {
			// TODO Prefer numbers than letters than special chars
			return 0;
		}
		if (o1.getClass().isArray()) {
			return compareArrays(o1, o2);
		}
		Log.warning("Don't know how to compare elements of type " + o1.getClass().getSimpleName() + ".");
		return 0;
	}

	private int compareArrays(Object o1, Object o2) {
		int len1 = Array.getLength(o1);
		int len2 = Array.getLength(o2);
		int res = Integer.compare(len1, len2);
		// Prefer the shortest array:
		if (res != 0) {
			return res;
		}
		// If same, prefer the array with less complex elements:
		return compareArrayValues(o1, o2);
	}

	private int compareArrayValues(Object array1, Object array2) {
		int sum = 0;
		int firstDiff = 0;
		for (int i = 0; i < Array.getLength(array1); ++i) {
			int comp = compare(Array.get(array1, i), Array.get(array2, i));
			int normalized = comp == 0 ? 0 : comp < 0 ? -1 : 1;
			if (firstDiff == 0 && normalized != 0) {
				firstDiff = normalized;
			}
			sum += normalized;
		}
		if (sum != 0) {
			return sum;
		}
		return firstDiff;
	}

}
