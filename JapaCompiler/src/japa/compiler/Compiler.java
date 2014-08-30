/*
 * Created on 14/12/2006
 */
package japa.compiler;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
public final class Compiler {

	public static final String APPLICATION_DEVELOPER = "Júlio Vilmar Gesser (jgeser@gmail.com)";

	public static final String APPLICATION_VERSION = "1.0";

	public static final String APPLICATION_NAME = "JAPA";

	public final Map<File, CompilationUnit> parseds;

	public final Map<File, CompilationUnit> resolveds;

	public final File[] sourcePath;

	public final FileManager fileManager;

	private static Logger logger = new NullLogger();

	public static Logger getLogger() {
		return logger;
	}

	public static void compile(File[] classPath, File[] sourcePath, File[] files, boolean verbose)
			throws CompilationException {
		logger = verbose ? new VerboseLogger() : new NullLogger();
		new Compiler(classPath, sourcePath).compile(files);
	}

	public static CompilationUnit compile(File[] classPath, File[] sourcePath, File file, boolean verbose)
			throws CompilationException {
		logger = verbose ? new VerboseLogger() : new NullLogger();
		return new Compiler(classPath, sourcePath).compile(file);
	}

	public Compiler(File[] classPath, File[] sourcePath) {
		this.sourcePath = sourcePath;
		this.parseds = new HashMap<File, CompilationUnit>();
		this.resolveds = new HashMap<File, CompilationUnit>();
		this.fileManager = new FileManager(this, classPath, sourcePath);
	}

	private void addFilesTree(File file, List<File> fileList) {
		if (file.isDirectory()) {

			File[] listFiles = file.listFiles(new FileFilter() {

				public boolean accept(File f) {
					return f.getName().endsWith(".java") || f.isDirectory();
				}

			});
			for (File f : listFiles) {
				addFilesTree(f, fileList);
			}
		} else {
			fileList.add(file);
		}
	}

	private CompilationUnit compile(File file) throws CompilationException {

		getLogger().log("Parsing file: " + file);
		CompilationUnit cu = parse(file);
		fileManager.addFile(file, cu);

		getLogger().log("Resolving file: " + file);
		CompilationUnit res = resolve(file);

		getLogger().log("Compiling file: " + file);
		compile(res, file);
		return res;
	}

	public void compile(File[] files) throws CompilationException {
		List<File> fileList = new ArrayList<File>();
		for (File file : files) {
			if (file.isDirectory()) {
				getLogger().log("Searching for source files in directory: " + file);
				addFilesTree(file, fileList);
			} else {
				fileList.add(file);
			}
		}

		for (File file : fileList) {
			getLogger().log("Parsing file: " + file);
			CompilationUnit cu = parse(file);
			fileManager.addFile(file, cu);
		}
		for (File file : fileList) {
			getLogger().log("Resolving file: " + file);
			CompilationUnit cu = resolve(file);

			getLogger().log("Compiling file: " + file);
			compile(cu, file);
		}
	}
	
	private CompilationUnit parse(File file) throws CompilationException {
		try {
			// analize sintática
			CompilationUnit cu = JavaParser.parse(file);

			// cacheia
			parseds.put(getCannonicalFile(file), cu);

			return cu;
		} catch (ParseException e) {
			throw new CompilationException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private CompilationUnit resolve(File file) throws CompilationException {
		try {
			return internalResolve(file);
		} catch (ParseException e) {
			throw new CompilationException(e.getMessage(), e);
		} catch (SemanticException e) {
			throw new CompilationException(e.getMessage(), e);
		}
	}

	private CompilationUnit internalResolve(File file) throws ParseException, SemanticException {
		File cannonicalFile = getCannonicalFile(file);
		CompilationUnit cu = resolveds.get(cannonicalFile);
		if (cu != null) {
			return cu;
		}
		cu = parseds.get(cannonicalFile);

		// resolve nomes e tipos
		resolveds.put(cannonicalFile, cu);
		new TypesAndNamesResolverVisitor(this, file, cu).resolve();

		return cu;
	}

	private void compile(CompilationUnit cu, File file) throws CompilationException {
		try {
			// analize semantica
			new SemanticCheckerVisitor(this, file, cu).check();
		} catch (SemanticException e) {
			throw new CompilationException(e.getMessage(), e);
		}
	}

	/**
	 * Retorna o nome esperado para o pacote do arquivo, levando em consideração
	 * os diretórios do sourcepath. Se o arquivo não estiver no sourcepath, não
	 * há como saber seu pacote esperado.
	 * 
	 * @param file
	 *            java file
	 * @return
	 */
	public String getExpectedPackage(File file) {
		boolean found = false;
		StringBuilder ret = new StringBuilder();
		file = getCannonicalFile(file.getParentFile());
		loop: while (file != null) {
			for (int i = 0; i < sourcePath.length; i++) {
				if (file.equals(getCannonicalFile(sourcePath[i]))) {
					found = true;
					break loop;
				}
			}
			if (ret.length() > 0) {
				ret.insert(0, ".");
			}
			ret.insert(0, file.getName());
			file = file.getParentFile();
		}
		return found ? ret.toString() : null;
	}

	public CompilationUnit getCompilationUnit(File file) throws ParseException, SemanticException {
		return internalResolve(file);
	}
	
	public CompilationUnit getUnit(File file) {
		File cannonicalFile = getCannonicalFile(file);
		return resolveds.get(cannonicalFile);
	}

	private File getCannonicalFile(File file) {
		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return file;
	}

	public interface Logger {

		public void log(String msg);
	}

	public static class NullLogger implements Logger {

		public void log(String msg) {
		}

	}

	public static class VerboseLogger implements Logger {

		public void log(String msg) {
			System.out.println(msg);
		}
	}

}
