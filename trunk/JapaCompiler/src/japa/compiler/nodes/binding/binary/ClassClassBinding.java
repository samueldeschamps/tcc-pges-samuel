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
public final class ClassClassBinding extends BinaryClassBinding {

    private final File classFile;

    public ClassClassBinding(Compiler compiler, String id, String pkg, File classFile) {
        super(compiler, id, pkg);
        this.classFile = classFile;
    }

    @Override
    protected Class innerGetType() {
        try {
            int i = 0;
            String cname = getName();
            File f = classFile;
            while (i > -1) {
                i = cname.indexOf('.', i + 1);
                f = f.getParentFile();
            }
            return new URLClassLoader(new URL[] { f.toURI().toURL() }).loadClass(cname);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
