/*
 * Created on 05/05/2007
 */
package japa.compiler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Usage: <options> <source files | directories>
 * 
 * options:
 * 
 * -d <dir>         
 *          output directory (if omitted, current location is used)
 * -verbose
 *          enable verbose output
 * -time
 *          prints the compilation time
 * -sourcepath <directories separated by ;>
 *          aditional sources for compilation
 * -cp -classpath <directories or zip/jar files separated by ;>
 *          aditional classes for compilation
 * -version
 *          prints de compiler version
 * 
 * 
 * @author Julio Vilmar Gesser
 */
public class Main {

    private static final class Options {

        private boolean error;

        private boolean verbose;

        private boolean time;

        private File outDir;

        private final List<File> classpath = new ArrayList<File>();

        private final List<File> sourcepath = new ArrayList<File>();

        private final List<File> files = new ArrayList<File>();

        public Options(String[] args) {
            boolean noMoreOptions = false;
            for (int i = 0; i < args.length; i++) {
                if (!noMoreOptions) {
                    if (args[i].equals("-d")) {
                        i++;
                        if (i >= args.length) {
                            error = true;
                            System.out.println("Invalid number or arguments");
                            return;
                        }
                        File file = new File(args[i]);
                        if (file.exists() && !file.isDirectory()) {
                            error = true;
                            System.out.println("Output directory is not a directory: " + file);
                            return;
                        }
                        outDir = file;
                        continue;
                    }
                    if (args[i].equals("-version")) {
                        printVersion();
                        error = true;
                        return;
                    }
                    if (args[i].equals("-verbose")) {
                        verbose = true;
                        continue;
                    }
                    if (args[i].equals("-time")) {
                        time = true;
                        continue;
                    }
                    if (args[i].equals("-sourcepath")) {
                        i++;
                        if (i >= args.length) {
                            error = true;
                            System.out.println("Invalid number or arguments");
                            return;
                        }
                        File file = new File(args[i]);
                        if (!file.isDirectory()) {
                            error = true;
                            System.out.println("Sourcepath entry is not a directory: " + file);
                            return;
                        }
                        sourcepath.add(file);
                        continue;
                    }
                    if (args[i].equals("-cp") || args[i].equals("classpath")) {
                        i++;
                        if (i >= args.length) {
                            error = true;
                            System.out.println("Invalid number or arguments");
                            return;
                        }
                        File file = new File(args[i]);
                        if (file.isDirectory()) {
                            error = true;
                            System.out.println("Classepath entry cannot be a directory: " + file);
                            return;
                        }
                        classpath.add(file);
                        continue;
                    }
                    noMoreOptions = true;
                }
                // se não houver mais opções, o resto é
                files.add(new File(args[i]));
            }

        }

        public boolean hasError() {
            return error;
        }

        public boolean getTime() {
            return time;
        }

        public File[] getClasspath() {
            return classpath.toArray(new File[classpath.size()]);
        }

        public File[] getSourcepath() {
            return sourcepath.toArray(new File[sourcepath.size()]);
        }

        public File[] getFiles() {
            return files.toArray(new File[files.size()]);
        }

        public File getOutDir() {
            return outDir == null ? new File(".") : outDir;
        }

        public boolean getVerbose() {
            return verbose;
        }

    }

    private static void printVersion() {
        System.out.print(Compiler.APPLICATION_NAME);
        System.out.print(" ");
        System.out.print(Compiler.APPLICATION_VERSION);
        System.out.print(" developed by ");
        System.out.println(Compiler.APPLICATION_DEVELOPER);
    }

    public static void main(String... args) throws CompilationException {
        if (args == null || args.length == 0) {
            printCmdLineOptions();
            return;
        }

        Options options = new Options(args);
        if (options.hasError()) {
            return;
        }

        File[] bootClasspath = getBootClasspath();
        if (bootClasspath == null) {
            System.out.println("java.home property is not configured");
            return;
        }
        if (bootClasspath.length == 0) {
            System.out.println("Bootstrap libraries not found in lib dir");
            return;
        }

        File[] classpath = new File[bootClasspath.length + options.getClasspath().length];
        System.arraycopy(bootClasspath, 0, classpath, 0, bootClasspath.length);
        System.arraycopy(options.getClasspath(), 0, classpath, bootClasspath.length, options.getClasspath().length);

        long time = System.currentTimeMillis();
        Compiler.compile(classpath, options.getSourcepath(), options.getFiles(), options.getVerbose());
        if (options.getTime()) {
            System.out.println("[COMPILATION TIME] " + (System.currentTimeMillis() - time) + " ms");
        }
    }

    private static void printCmdLineOptions() {
        printVersion();
        System.out.println();
        System.out.println("Usage: <options> <source files | directories>");
        System.out.println();
        System.out.println("options:");
        System.out.println("    -d <dir>");
        System.out.println("                output directory (if omitted, current location is used)");
        System.out.println("    -verbose");
        System.out.println("                enable verbose output");
        System.out.println("    -time");
        System.out.println("                prints the compilation time");
        System.out.println("    -sourcepath <directories separated by ;>");
        System.out.println("                aditional sources for compilation");
        System.out.println("    -cp -classpath <directories or zip/jar files separated by ;>");
        System.out.println("                aditional classes for compilation");
        System.out.println("    -version");
        System.out.println("                prints de compiler version");
    }

    public static File[] getBootClasspath() {
        String javaHome = System.getProperty("java.home");
        File bootDir = new File(javaHome, "lib");
        if (!bootDir.exists()) {
            return null;
        }
        return bootDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".zip");
            }

        });
    }
}
