/*
 * Created on 24/01/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.Compiler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Julio Vilmar Gesser
 */
public final class JarClassBinding extends BinaryClassBinding {

    private final File jarFile;

    public JarClassBinding(Compiler compiler, String id, String pkg, File jarFile) {
        super(compiler, id, pkg);
        this.jarFile = jarFile;
    }

    @Override
    protected Class innerGetType() {
        try {
            return new URLClassLoader(new URL[] { jarFile.toURI().toURL() }).loadClass(getName());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
