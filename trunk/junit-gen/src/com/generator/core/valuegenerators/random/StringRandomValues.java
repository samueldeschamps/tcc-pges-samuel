package com.generator.core.valuegenerators.random;

import java.util.ArrayList;
import java.util.List;

import com.generator.core.ValueGenerator;
import com.generator.core.valuegenerators.ParallelValueGeneratorComposite;

public class StringRandomValues extends ParallelValueGeneratorComposite<String> {

	public StringRandomValues() {
		super(getStringGenerators());
	}

	private static List<ValueGenerator<String>> getStringGenerators() {
		List<ValueGenerator<String>> res = new ArrayList<>();
		res.add(new StringNumericRandomValues());
		res.add(new StringAlphabeticLowerRandomValues());
		res.add(new StringAlphabeticUpperRandomValues());
		res.add(new StringAlphabeticCapitalizedRandomValues());
		res.add(new StringAleatoryRandomValues());
		return res;
	}

}
