package com.generator.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	// TODO Refactor this hierarchy not to have the same dataStore in all child objects.
	private final ExecutionDataStore dataStore;
	private final Method method;
	private final String signatureDesc;
	private List<CoverageInfo> indirectCoverages;
	private Counter shallowCounter = null;

	public CoverageInfo(ExecutionDataStore dataStore, Method method) {
		this.dataStore = dataStore;
		this.method = method;
		this.signatureDesc = Util.getSignatureDesc(method);
		this.indirectCoverages = null;
	}

	public void addIndirectCoverage(CoverageInfo info) {
		if (this.indirectCoverages == null) {
			this.indirectCoverages = new LinkedList<>();
		}
		this.indirectCoverages.add(info);
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

	public Counter getShallowCounter() {
		if (shallowCounter == null) {
			shallowCounter = calculateCounter();
		}
		return shallowCounter;
	}

	public Counter getDeepCounter(int level) {
		if (level == 0) {
			return getShallowCounter();
		}
		Counter res = new Counter(0L, 0L);
		if (indirectCoverages != null) {
			for (CoverageInfo cov : indirectCoverages) {
				res = res.add(cov.getDeepCounter(level - 1));
			}
		}
		return res;
	}

	private Counter calculateCounter() {
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(dataStore, coverageBuilder);
		InputStream byteCode = Util.getClassBytecodeAsStream(getDeclaringClass());
		try {
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
								ICounter instCounter = mc.getInstructionCounter();
								return new Counter(instCounter.getCoveredCount(), instCounter.getTotalCount());
							}
						}
					}
				}
				String msg = "Could not find coverage ratio for method '%s' with signature '%s'.";
				Log.error(String.format(msg, method.getName(), signatureDesc));
			} finally {
				byteCode.close();
			}
		} catch (IOException ex) {
			Log.error("Error calculating coverage ratio for method '" + method.getName() + "'.", ex);
		}
		return null;
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
		CoverageInfo copy = new CoverageInfo(dataStore.copy(), method);
		if (indirectCoverages != null) {
			for (CoverageInfo info : indirectCoverages) {
				copy.addIndirectCoverage(info.copy());
			}
		}
		return copy;
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
		if ((this.indirectCoverages == null) != (other.indirectCoverages == null)) {
			Log.error("CoverageInfos with incompatible inner coverage infos.");
			return;
		}
		if (this.indirectCoverages != null) {
			if (this.indirectCoverages.size() != other.indirectCoverages.size()) {
				Log.error("CoverageInfos with incompatible inner coverage infos.");
				return;
			}
			for (int i = 0; i < indirectCoverages.size(); ++i) {
				indirectCoverages.get(i).mergeWith(other.indirectCoverages.get(i));
			}
		}
	}

	@Override
	public String toString() {
		Counter counter = getShallowCounter();
		return String.valueOf(counter.getCovered() + "/" + counter.getTotal());
	}

	// TODO Improve performance
	public int compareCoverages(CoverageInfo other, int maxDepth) {
		for (int i = 0; i <= maxDepth; ++i) {
			int res = this.getDeepCounter(i).compareTo(other.getDeepCounter(i));
			if (res != 0) {
				return res;
			}
		}
		return 0;
	}

}
