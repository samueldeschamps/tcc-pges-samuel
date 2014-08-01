package com.generator.structure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {

	private JUnitGenerator jUnitGenerator;
	private final Method method;
	private final InnerExecutor executor;
	private final List<TestCaseData> result = new ArrayList<>();

	public TestDataGenerator(JUnitGenerator jUnitGenerator, Method method) {
		this.jUnitGenerator = jUnitGenerator;
		this.method = method;
		this.executor = new InnerExecutor(method);
	}

	public void execute() {
		result.clear();

		ValueGeneratorRegistry registry = jUnitGenerator.getValueGenerators();
		ValueSetGenerator paramValuesGen = new ValueSetGenerator(registry, method.getParameterTypes());

		// TODO Implementar as condições de parada (Cobertura, Min. Testes,
		// Todos retornos possíveis, etc).

		int count = 0;
		while (paramValuesGen.hasNext() && count < 5) {

			List<Object> paramValues = paramValuesGen.next();
			ExecutionResult execResult = executor.execute(paramValues);
			TestCaseData caseData = new TestCaseData(paramValues, execResult);
			if (caseData.getResult().failed()) {
				switch (jUnitGenerator.getExceptionsStrategy()) {
				case IGNORE_ALWAYS:
					continue;
				case IGNORE_WHEN_NOT_DECLARED:
					if (!caseData.getResult().isExceptionDeclared()) {
						continue;
					}
					break;
				default:
					break;
				}
			}

			result.add(caseData);
			count++;
		}
	}

	public List<TestCaseData> getResult() {
		return result;
	}

}
