package com.generator.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;

import com.generator.core.util.Log;
import com.generator.core.util.Util;

public class CoverageInfo {

	private final ExecutionDataStore dataStore;
	private final Method method;
	private final String signatureDesc;
	private double coverageRatio = -1.0;

	public CoverageInfo(ExecutionDataStore dataStore, Method method) {
		this.dataStore = dataStore;
		this.method = method;
		this.signatureDesc = getSignatureDesc(method);
	}

	private String getSignatureDesc(Method m) {
		StringBuilder res = new StringBuilder();
		res.append("(");
		for (Class<?> param : m.getParameterTypes()) {
			res.append(getTypeDesc(param));
		}
		res.append(")");
		res.append(getTypeDesc(m.getReturnType()));
		return res.toString();
	}

	private String getTypeDesc(Class<?> type) {
		if (type == boolean.class) {
			return "Z";
		}
		if (type == byte.class) {
			return "B";
		}
		if (type == short.class) {
			return "S";
		}
		if (type == char.class) {
			return "C";
		}
		if (type == int.class) {
			return "I";
		}
		if (type == long.class) {
			return "J";
		}
		if (type == float.class) {
			return "F";
		}
		if (type == Double.TYPE) {
			return "D";
		}
		if (type.isArray()) {
			return "[" + getTypeDesc(type.getComponentType());
		}
		if (type == Void.TYPE) {
			return "V";
		}
		return "L" + type.getName().replace(".", "/") + ";";
	}

	public ExecutionDataStore getDataStore() {
		return dataStore;
	}

	public String getClassName() {
		return getDeclaringClass().getName();
	}

	public Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}

	public Method getMethod() {
		return method;
	}

	public double getCoverageRatio() {
		if (coverageRatio == -1.0) {
			try {
				coverageRatio = calculateCoverageRatio();
			} catch (IOException ex) {
				Log.error("Error calculating coverage ratio for method '" + method.getName() + "'.", ex);
				coverageRatio = 0.0;
			}
		}
		return coverageRatio;
	}

	private double calculateCoverageRatio() throws IOException {
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(dataStore, coverageBuilder);
		InputStream byteCode = Util.getClassBytecodeAsStream(getDeclaringClass());
		try {
			String targetName = getClassName();
			analyzer.analyzeClass(byteCode, targetName);
			targetName = targetName.replace(".", "/");

			for (IClassCoverage cc : coverageBuilder.getClasses()) {
				if (targetName.equals(cc.getName())) {
					for (IMethodCoverage mc : cc.getMethods()) {
						// TODO Use the JaCoCo StringPool to improve
						// performance?
						if (method.getName().equals(mc.getName()) && signatureDesc.equals(mc.getDesc())) {
							ICounter counter = mc.getInstructionCounter();
							return counter.getCoveredRatio();
						}
					}
				}
			}
			String msg = "Could not find coverage ratio for method '%s' with signature '%s'.";
			Log.error(String.format(msg, method.getName(), signatureDesc));
			return 0.0;
		} finally {
			byteCode.close();
		}
	}

	public static CoverageInfo merge(CoverageInfo... infos) {
		if (infos == null || infos.length == 0) {
			return null;
		}
		CoverageInfo res = infos[0].copy();
		for (int i = 1; i < infos.length; ++i) {
			res.mergeWith(infos[i]);
		}
		return res;
	}

	public CoverageInfo copy() {
		return new CoverageInfo(dataStore.copy(), method);
	}

	/**
	 * This method changes the content of <code>this</code> object.
	 */
	private void mergeWith(CoverageInfo other) {
		if (!this.method.equals(other.method)) {
			Log.error("Could not merge coverage from different methods!");
			return;
		}
		Collection<ExecutionData> thisData = this.dataStore.getContents();
		Collection<ExecutionData> otherData = other.dataStore.getContents();
		if (thisData.size() != otherData.size()) {
			Log.error("CoverageInfos with incompatible data stores.");
			return;
		}
		Iterator<ExecutionData> otherIt = otherData.iterator();
		for (ExecutionData thisItem : thisData) {
			thisItem.merge(otherIt.next());
		}
	}

	@Override
	public String toString() {
		return String.valueOf(getCoverageRatio());
	}

}
