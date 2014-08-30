/*
 * Created on 12/12/2006
 */
package japa.compiler.nodes.binding;

import japa.compiler.nodes.namespace.INameSpace;
import japa.compiler.nodes.namespace.PackageNameSpace;

/**
 * @author Julio Vilmar Gesser
 */
public class PackageBinding extends Binding {

    private final String pkg;

    private PackageNameSpace pns;

    private final japa.compiler.Compiler compiler;

    public PackageBinding(japa.compiler.Compiler compiler, String parentPkg, String pkg) {
        this(compiler, parentPkg.length() > 0 ? parentPkg + "." + pkg : pkg);
    }

    public PackageBinding(japa.compiler.Compiler compiler, String pkg) {
        this.pkg = pkg;
        this.compiler = compiler;
    }

    @Override
    public INameSpace getNameSpace() {
        if (pns == null) {
            pns = new PackageNameSpace(compiler.fileManager, pkg);
        }
        return pns;
    }

    @Override
    public String getName() {
        return pkg;
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        throw new IllegalStateException();
    }

    @Override
    public Binding getCommonType(Binding b) {
        throw new IllegalStateException();
    }

}
