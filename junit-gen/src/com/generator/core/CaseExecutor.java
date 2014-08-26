package com.generator.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import com.generator.core.util.Log;
import com.generator.core.util.Util;

public class CaseExecutor {

	private final Method method;
	private final CallNode callHierarchy;
	private final Set<Class<?>> usedClasses = new LinkedHashSet<>();

	public CaseExecutor(JUnitGenerator jUnitGenerator, Method method) {
		this.method = method;
		this.callHierarchy = jUnitGenerator.getCodeInfo().getCallHierarchy(method);
		if (callHierarchy != null) {
			addInvolvedClasses(callHierarchy);
		}
	}

	private void addInvolvedClasses(CallNode node) {
		usedClasses.add(node.getMethod().getDeclaringClass());
		if (node.hasChildren()) {
			for (CallNode child : node.getCalleds()) {
				addInvolvedClasses(child);
			}
		}
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

	// TODO Improve code quality
	private ExecutionResult invokeCoverage(List<Object> params) throws IOException, Exception, ClassNotFoundException {
		
		final InputStream[] byteCodeInputs = new InputStream[usedClasses.size()];
		int i = 0;
		for (Class<?> clazz : usedClasses) {
			byteCodeInputs[i++] = Util.getClassBytecodeAsStream(clazz);
		}
		
		final IRuntime runtime = new LoggerRuntime();
		final byte[][] instrumenteds = new byte[usedClasses.size()][];
		int j = 0;
		for (Class<?> clazz : usedClasses) {
			instrumenteds[j] = new Instrumenter(runtime).instrument(byteCodeInputs[j], clazz.getName());
			j++;
		}
		
		final RuntimeData data = new RuntimeData();
		runtime.startup(data);
		try {
			final MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
			int k = 0;
			for (Class<?> clazz : usedClasses) {
				memoryClassLoader.addDefinition(clazz.getName(), instrumenteds[k]);
				k++;
			}
			final Class<?> instrumentedClass = memoryClassLoader.loadClass(method.getDeclaringClass().getName());
			
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
			
			return new ExecutionResult(result, mountCoverageInfo(executionData, callHierarchy));
		} finally {
			runtime.shutdown();
		}
	}

	private CoverageInfo mountCoverageInfo(ExecutionDataStore executionData, CallNode node) {
		CoverageInfo root = new CoverageInfo(executionData, node.getMethod());
		if (node.hasChildren()) {
			for (CallNode child : node.getCalleds()) {
				root.addIndirectCoverage(mountCoverageInfo(executionData, child));
			}
		}
		return root;
	}

	private ExecutionResult invokeMethod(Method method, List<Object> params) {
		try {
			if (containsArrays(params)) {
				normalizeArrays(params, method);
			}
			Object callRes = method.invoke(null, params.toArray());
			return new ExecutionResult(callRes);

		} catch (InvocationTargetException ex) {

			boolean declared = false;
			Throwable cause = ex.getCause();
			if (cause != null) {
				if (cause instanceof VirtualMachineError) {
					throw new RuntimeException("Fatal JVM error with params: " + paramsToStr(params),
							(VirtualMachineError) cause);
				}
				for (Class<?> throwEx : method.getExceptionTypes()) {
					if (throwEx.isAssignableFrom(cause.getClass())) {
						declared = true;
					}
				}
			}
			return new ExecutionResult(cause, declared);

		} catch (IllegalAccessException | IllegalArgumentException e) {

			Log.error("Error invoking method '" + method.getName() + "'.", e);
			return null;
		}
	}

	private String paramsToStr(List<Object> params) {
		return Arrays.toString(params.toArray());
	}

	private void normalizeArrays(List<Object> paramValues, Method method) {
		for (int i = 0; i < paramValues.size(); ++i) {
			Object paramVal = paramValues.get(i);
			if (paramVal == null) {
				continue;
			}
			Class<? extends Object> type = paramVal.getClass();
			if (paramVal == null || !type.isArray()) {
				continue;
			}
			Class<?> paramType = method.getParameterTypes()[i];
			if (type.equals(paramType)) {
				continue;
			}
			Class<?> compType = paramType.getComponentType();
			if (paramType.getComponentType().isPrimitive()
					&& Util.primitiveToWrapper(compType).equals(type.getComponentType())) {
				paramValues.set(i, convertArrayToPrimitive(paramVal, compType));
			}
		}
	}

	private Object convertArrayToPrimitive(Object paramVal, Class<?> primitiveType) {
		int length = Array.getLength(paramVal);
		Object res = Array.newInstance(primitiveType, length);
		for (int i = 0; i < length; ++i) {
			Array.set(res, i, Array.get(paramVal, i));
		}
		return res;
	}

	private boolean containsArrays(List<Object> params) {
		for (Object param : params) {
			if (param != null && param.getClass().isArray()) {
				return true;
			}
		}
		return false;
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
