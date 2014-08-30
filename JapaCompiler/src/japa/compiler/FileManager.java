/*
 * Created on 24/01/2007
 */
package japa.compiler;

import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.compiler.nodes.binding.PackageBinding;
import japa.compiler.nodes.binding.binary.ClassClassBinding;
import japa.compiler.nodes.binding.binary.JarClassBinding;
import japa.compiler.nodes.binding.source.SourceClassBinding;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.Expression;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Classe responsável por resolver nomes de arquivos e pacotes.
 * 
 * @author Julio Vilmar Gesser
 */
public class FileManager implements INameSpace {

    public static final String JAVA = "java";

    public static final String JAVA_LANG = JAVA + ".lang";

    public static final String JAVA_LANG_STRING = JAVA_LANG + ".String";

    private final Object JAR_FULL_LOADED = new Object();

    private final Object ENTRIES = new Object();

    private static final String SRC_EXT = ".java";

    private static final String BIN_EXT = ".class";

    private final Compiler compiler;

    private final File[] classPath;

    private final File[] sourcePath;

    /**
     * Cache de arquivos jar e suas entradas, formato: key = File (caminho do
     * jar) value = Map (leia abaixo) a chave e o valor do segundo mapa podem
     * variar de tipo, seguindo as configurações: [ Representação do arquivo Jar
     * ] key = File (mesma chave do mapa anterior) value = JarFile (arquivo jar)
     * [ Cache de binding já resolvido ] key = String (nome do arquivo
     * requerido) value = Binding (Binding correspondente ao arquivo) [ Cache de
     * nome de entrada do jar, prefine iterações desnecessárias sobre as
     * entradas do jar ] key = String (nome do arquivo requirido) value = String
     * ( = a chave)
     */
    private final Map<File, Map<Object, Object>> jarCache;

    /**
     * Cache de bindings. key = Nome completo da classe separado por "/", ex:
     * java/lang/String value = Binding
     */
    private final Map<String, Binding> bindingCache;

    public FileManager(Compiler compiler, File[] classPath, File[] sourcePath) {
        this.compiler = compiler;
        this.classPath = classPath;
        this.sourcePath = sourcePath;
        jarCache = new HashMap<File, Map<Object, Object>>();
        bindingCache = new HashMap<String, Binding>();
    }

    /**
     * Busca um arquivo .class, .java ou pacote no classPath e sourcePath.
     * 
     * @param pkg
     *            pacote recipiente
     * @param id
     *            nome do item peocurado
     * @return
     */
    public Binding getBinding(String pkg, String id) {
        String fileName = pkg.length() > 0 ? pkg.replace('.', '/') + "/" + id : id;

        Binding ret = bindingCache.get(fileName);
        if (ret != null) {
            return ret;
        }

        ret = findInSourcePath(pkg, id, fileName);
        if (ret == null) {
            ret = findInClassPath(pkg, id, fileName);
        }
        return ret;
    }

    /**
     * Busca um binding apenas se ele já foi carregado
     * 
     * @param pkg
     *            pacote recipiente
     * @param id
     *            nome do item peocurado
     * @return
     */
    public Binding getBindingFromCache(String pkg, String id) {
        String fileName = pkg.length() > 0 ? pkg.replace('.', '/') + "/" + id : id;
        return bindingCache.get(fileName);
    }

    public Binding getBinding(String fullId) {
        int i = fullId.lastIndexOf('.');
        if (i >= 0) {
            return getBinding(fullId.substring(0, i), fullId.substring(i + 1, fullId.length()));
        }
        return getBinding("", fullId);
    }

    /**
     * Busca um arquivo .class ou pacote nas entradas do classPath.
     * 
     * @param pkg
     *            pacote recipiente
     * @param id
     *            nome do item peocurado
     * @param fileName
     *            nome do arquivo peocurado
     * @return
     */
    private Binding findInClassPath(String pkg, String id, String fileName) {
        Binding ret = null;
        for (File element : classPath) {
            if (element.isDirectory()) {
                ret = findInClassPathDir(element, pkg, id, fileName);
            } else {
                ret = findInClassPathJar(element, pkg, id, fileName);
            }
            if (ret != null) {
                break;
            }
        }
        return ret;
    }

    /**
     * Busca um arquivo .class ou pacote em um jar.
     * 
     * @param jarFile
     *            arquivo jar
     * @param pkg
     *            pacote recipiente
     * @param id
     *            nome do item peocurado
     * @param fileName
     *            nome do arquivo peocurado
     * @return
     */
    private Binding findInClassPathJar(File jarFile, String pkg, String id, String fileName) {
        // verifica se existe cache para o jar
        Map<Object, Object> cache = jarCache.get(jarFile);
        if (cache == null) {
            // cacheia o cache deste jar
            cache = new HashMap<Object, Object>();
            jarCache.put(jarFile, cache);
        }

        // verifica se o é uma classe no cache
        Object obj = cache.get(fileName + BIN_EXT);
        if (obj != null) {
            // resolve e cacheia
            JarClassBinding ret = new JarClassBinding(compiler, id, pkg, jarFile);
            bindingCache.put(fileName, ret);
            cache.remove(fileName + BIN_EXT);
            return ret;
        }

        // verifica se o é um pacote no cache
        obj = cache.get(fileName);
        if (obj != null) {
            // resolve e cacheia
            assert fileName.equals(obj);
            PackageBinding ret = new PackageBinding(compiler, pkg, id);
            bindingCache.put(fileName, ret);
            cache.remove(fileName);
            return ret;
        }

        // verifica se o jar já foi totalmente scaneado
        if (!cache.containsKey(JAR_FULL_LOADED)) {

            try {
                JarFile jar = (JarFile) cache.get(jarFile);

                Enumeration<JarEntry> entries;
                if (jar == null) {
                    jar = new JarFile(jarFile);
                    cache.put(jarFile, jar);

                    entries = jar.entries();
                    cache.put(ENTRIES, entries);
                } else {
                    entries = (Enumeration<JarEntry>) cache.get(ENTRIES);
                }

                // varre o jar para cachear as entradas
                while (entries.hasMoreElements()) {
                    // cacheia a classe
                    String name = entries.nextElement().getName();
                    if (name.equals(fileName + BIN_EXT)) {
                        JarClassBinding ret = new JarClassBinding(compiler, id, pkg, jarFile);
                        bindingCache.put(fileName, ret);
                        return ret;
                    }
                    cache.put(name, name);

                    PackageBinding ret = null;
                    int i = name.lastIndexOf('/');
                    while (i > -1) {
                        name = name.substring(0, i);

                        if (ret == null && fileName.equals(name)) {
                            ret = (PackageBinding) bindingCache.get(fileName);
                            if (ret == null) {
                                ret = new PackageBinding(compiler, pkg, id);
                                bindingCache.put(fileName, ret);
                            }
                            // mesmo que encontrou pacote tem q alimentar o cache com todas as pastas deste arquivo
                        }

                        if (bindingCache.get(name) == null) {
                            cache.put(name, name);
                        }

                        i = name.lastIndexOf('/');
                    }
                    if (ret != null) {
                        return ret;
                    }
                }

                // marca o jar como totalmente scaneado
                jar.close();
                cache.remove(fileName);
                cache.remove(ENTRIES);
                cache.put(JAR_FULL_LOADED, JAR_FULL_LOADED);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;

    }

    /**
     * Busca um arquivo .class ou pacote em uma pasta.
     * 
     * @param classPathEntry
     *            pasta recipiente
     * @param pkg
     *            pacote recipiente
     * @param id
     *            nome do item peocurado
     * @param fileName
     *            nome do arquivo peocurado
     * @return
     */
    private Binding findInClassPathDir(File classPathEntry, String pkg, String id, String fileName) {
        File file = new File(classPathEntry, fileName);
        if (file.exists() && file.isDirectory()) {
            try {
                // esse if gerante que não haja confusão com letras maiusclas/minusculas
                if (file.getCanonicalPath().replace('\\', '/').endsWith(fileName)) {
                    Binding ret = new PackageBinding(compiler, pkg, id);
                    bindingCache.put(fileName, ret);
                    return ret;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        file = new File(classPathEntry, fileName + BIN_EXT);
        if (file.exists()) {
            Binding ret = new ClassClassBinding(compiler, id, pkg, file);
            bindingCache.put(fileName, ret);
            return ret;
        }
        return null;
    }

    /**
     * Busca um arquivo .java ou pacote em uma pasta.
     * 
     * @param pkg
     *            pacote recipiente
     * @param id
     *            nome do item peocurado
     * @param fileName
     *            nome do arquivo peocurado
     * @return
     */
    private Binding findInSourcePath(String pkg, String id, String fileName) {
        File file = null;
        for (File element : sourcePath) {
            File packageFile = new File(element, fileName);
            File javaFile = new File(element, fileName + SRC_EXT);

            if (packageFile.exists() && packageFile.isDirectory()) {
                try {
                    // esse if gerante que não haja confusão com letras maiusclas/minusculas
                    if (packageFile.getCanonicalPath().replace('\\', '/').endsWith(fileName)) {
                        file = packageFile;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (javaFile.exists()) {
                if (file != null) {
                    throw new RuntimeSemanticException("The package name {0} colides with type", new Object[] { pkg }, 0, 0, file);
                }
                file = javaFile;
            }
            if (file != null) {
                break;
            }
        }
        if (file == null) {
            return null;
        }

        Binding ret;
        if (file.isDirectory()) {
            ret = new PackageBinding(compiler, pkg, id);
        } else {
            ret = new SourceClassBinding(compiler, id, pkg, file);
        }
        bindingCache.put(fileName, ret);
        return ret;
    }

    public Binding resolve(String id) {
        return getBinding("", id);
    }

    public MemberBinding resolve(String id, List<Expression> args) {
        return null;
    }

    public Binding resolveClass(String id) {
        return resolve(id);
    }

    /**
     * Retorna todos os Bindings no pacote informado (não busca no classpath),
     * apenas no sourcepath no cache.
     */
    public Binding[] getBindings(String pkg) {
        String pkgName = pkg.length() > 0 ? pkg.replace('.', '/') : "";

        Set<Binding> ret = new HashSet<Binding>();

        // procura no source path
        for (File element : sourcePath) {
            File packageFile = new File(element, pkgName);

            if (packageFile.exists() && packageFile.isDirectory()) {
                File[] files = packageFile.listFiles(new FileFilter() {

                    public boolean accept(File file) {
                        if (file.getName().endsWith(SRC_EXT)) {
                            return true;
                        }
                        return false;
                    }
                });

                for (File file : files) {
                    String id = file.getName().substring(0, file.getName().indexOf('.'));
                    String fileName = pkgName.length() > 0 ? pkgName + "/" + id : id;

                    Binding b = bindingCache.get(fileName);
                    if (b == null) {
                        b = new SourceClassBinding(compiler, id, pkg, file);
                        bindingCache.put(fileName, b);
                    }
                    ret.add(b);

                }
            }
        }

        // busca no cache
        for (Object element : bindingCache.values()) {
            Binding b = (Binding) element;
            if (b instanceof AbstractClassBinding) {
                if (((AbstractClassBinding) b).getPackage().equals(pkg)) {
                    ret.add(b);
                }
            }
        }

        return ret.toArray(new Binding[ret.size()]);
    }

    /**
     * Adiciona um binding apartir de um arquivo a ser compilado
     * 
     * @param file
     * @param cu
     */
    public void addFile(File file, CompilationUnit cu) {
        for (TypeDeclaration type : cu.getTypes()) {
            String id = type.getName();
            String pkg = cu.getPackage() == null ? "" : cu.getPackage().getName().toString();
            String fileName = pkg.length() > 0 ? pkg.replace('.', '/') + "/" + id : id;
            bindingCache.put(fileName, new SourceClassBinding(compiler, id, pkg, file));

            while (pkg.length() > 0) {
                fileName = pkg.replace('.', '/');
                if (bindingCache.get(fileName) == null) {
                    bindingCache.put(fileName, new PackageBinding(compiler, pkg));
                }
                int i = pkg.lastIndexOf('.');
                if (i >= 0) {
                    pkg = pkg.substring(0, i);
                } else {
                    pkg = "";
                }
            }
        }
    }
}
