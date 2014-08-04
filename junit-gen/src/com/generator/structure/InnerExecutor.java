package com.generator.structure;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

public class InnerExecutor {

	private final Method method;

	public InnerExecutor(Method method) {
		this.method = method;
	}

	public ExecutionResult execute(List<Object> params) {
		return invokeMethod(method, params);
	}

	public ExecutionResult executeCoverage(List<Object> params) {
		try {
			return invokeCoverage(params);
		} catch (Exception ex) {
			Log.error("Error collecting coverage.", ex);
			return null;
		}
	}

	private ExecutionResult invokeCoverage(List<Object> params) throws IOException, Exception, ClassNotFoundException {
		final Class<?> clazz = method.getDeclaringClass();
		final String resource = '/' + clazz.getName().replace('.', '/') + ".class";
		final InputStream byteCodeInput = clazz.getResourceAsStream(resource);
		final IRuntime runtime = new LoggerRuntime();
		final String className = clazz.getName();
		final byte[] instrumented = new Instrumenter(runtime).instrument(byteCodeInput, className);

		final RuntimeData data = new RuntimeData();
		runtime.startup(data);
		try {
			final MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
			memoryClassLoader.addDefinition(className, instrumented);
			final Class<?> instrumentedClass = memoryClassLoader.loadClass(className);

			Method covMethod = getCoverageMethod(instrumentedClass);
			if (covMethod == null) {
				return null;
			}
			ExecutionResult result = invokeMethod(covMethod, params);
			if (result == null) {
				return null;
			}
			final ExecutionDataStore executionData = new ExecutionDataStore();
			final SessionInfoStore sessionInfos = new SessionInfoStore();
			data.collect(executionData, sessionInfos, false);

			CoverageInfo covInfo = new CoverageInfo(executionData, method);
			return new ExecutionResult(result, covInfo);
		} finally {
			runtime.shutdown();
		}
	}

	private ExecutionResult invokeMethod(Method method, List<Object> params) {
		try {
			Object callRes = method.invoke(null, params.toArray());
			return new ExecutionResult(callRes);

		} catch (InvocationTargetException ex) {

			boolean declared = false;
			if (ex.getCause() != null) {
				for (Class<?> throwEx : method.getExceptionTypes()) {
					if (throwEx.isAssignableFrom(ex.getCause().getClass())) {
						declared = true;
					}
				}
			}
			return new ExecutionResult(ex.getCause(), declared);

		} catch (IllegalAccessException | IllegalArgumentException e) {

			Log.error("Error invoking method '" + method.getName() + "'.", e);
			return null;
		}
	}

	private Method getCoverageMethod(final Class<?> instrumentedClass) {
		for (Method m : instrumentedClass.getDeclaredMethods()) {
			if (!m.getName().equals(method.getName())) {
				continue;
			}
			if (!Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
				continue;
			}
			return m;
		}
		Log.error("Could not find a coverage equivalent method for '" + method.getName() + "'.");
		return null;
	}

}
