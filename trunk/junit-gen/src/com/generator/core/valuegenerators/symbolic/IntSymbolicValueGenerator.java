package com.generator.core.valuegenerators.symbolic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		// TODO Otimizar desempenho disparando a busca somente uma vez para a
		// classe inteira!
		List<Map<String, Integer>> allValues = searchCornerValues();
		filterByThisParameter(allValues);
	}

	private void filterByThisParameter(List<Map<String, Integer>> allValues) {
		String paramName = getParamName();
		for (Map<String, Integer> tuple : allValues) {
			Integer value = tuple.get(paramName);
			if (value == null) {
				Log.warning("Didn't generate value for parameter '" + paramName + "'.");
				continue;
			}
			values.add(value);
		}
	}

	private List<Map<String, Integer>> searchCornerValues() {
		String file = Util.getBytecodeFilePath(getMethod().getDeclaringClass());
		ByteCodeAnalyser analyser = new ByteCodeAnalyser(file);

		List<Map<String, Integer>> result = new ArrayList<>();
		for (CFG cfg : analyser.getCFGs()) {
			if (!isThisMethod(cfg)) {
				continue;
			}
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
							Map<String, Integer> map = new HashMap<>();
//							System.out.print("[");
							for (int i = 0; i < inputs.size(); ++i) {
								InputData input = inputs.get(i);
								map.put(input.getVariable(), Integer.valueOf(input.getValue()));
//								System.out.print(input.getVariable() + ": " + input.getValue() + ",   ");
							}
//							System.out.println("]");
							result.add(map);
						}
					}
				}
			}
		}
		return result;
	}

	private boolean isThisMethod(CFG cfg) {
		if (!getMethod().getName().equals(cfg.getMethodName())) {
			return false;
		}
		if (!Util.getSignatureDesc(getMethod()).equals(cfg.getMethodSignature())) {
			return false;
		}
		return true;
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
