package com.generator.core.valuegenerators.symbolic;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import profiling.constraint.analysis.Constraint;
import profiling.constraint.analysis.ConstraintSequenceGenerator;
import profiling.constraint.analysis.Path;
import profiling.constraint.analysis.PathGenerator;
import profiling.constraint.bytecode.ByteCodeAnalyser;
import profiling.constraint.graph.CFG;
import symbolic.execution.data.generation.ConstraintSequenceSolution;
import symbolic.execution.data.generation.InputData;
import symbolic.execution.data.generation.Solution;

import com.generator.core.ValueGenerationStrategy;
import com.generator.core.util.Log;
import com.generator.core.util.Util;
import com.generator.core.valuegenerators.MethodContextValueGenerator;

public class IntSymbolicValueGenerator extends MethodContextValueGenerator<Integer> {

	private List<Integer> values = new ArrayList<>();
	private int idx = 0;

	public IntSymbolicValueGenerator() {
		super();
		List<Integer[]> allValues = searchCornerValues();
		filterByThisParameter(allValues);
	}

	private void filterByThisParameter(List<Integer[]> allValues) {
		int paramIdx = getParamIdx();
		for (Integer[] tuple : allValues) {
			if (paramIdx >= tuple.length) {
				Log.warning("Searching a param index " + paramIdx + " in a " + tuple.length + " length tuple.");
				continue;
			}
			values.add(tuple[paramIdx]);
		}
	}

	// FIXME Os valores retornados podem não estar na ordem de declaração dos
	// parâmetros!
	private List<Integer[]> searchCornerValues() {
		String file = Util.getBytecodeFilePath(getMethod().getDeclaringClass());
		ByteCodeAnalyser analyser = new ByteCodeAnalyser(file);

		List<Integer[]> result = new ArrayList<>();
		for (CFG cfg : analyser.getCFGs()) {
			PathGenerator pathGen = new PathGenerator();
			ConstraintSequenceGenerator seqGen = new ConstraintSequenceGenerator();
			Vector<Path> paths = pathGen.generateAllPaths(cfg, 2);
			for (Path path : paths) {
				Vector<Constraint> constraints = seqGen.getConstraintSequenceElements(cfg, path);
				ConstraintSequenceSolution css = new ConstraintSequenceSolution(constraints);
				if (css.isSolvable()) {
					Solution solution = css.getSolution();
					if (solution != null) {
						Vector<InputData> inputs = solution.getInputData();
						if (inputs.size() > 0) {
							Integer[] resItem = new Integer[inputs.size()];
							for (int i = 0; i < resItem.length; ++i) {
								InputData input = inputs.get(i);
								resItem[i] = Integer.valueOf(input.getValue());
								// System.out.println(input.getVariable() + ": "
								// +
								// input.getValue());
							}
							result.add(resItem);
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean hasNext() {
		return idx < values.size();
	}

	@Override
	public Integer next() {
		return values.get(idx++);
	}

	@Override
	public ValueGenerationStrategy getStrategy() {
		return ValueGenerationStrategy.DATA_FLOW_STRATEGY;
	}

}
