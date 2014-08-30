/*
 * Created on 03/03/2007
 */
package japa.compiler.nodes.binding;

import japa.compiler.Compiler;
import japa.compiler.FileManager;
import japa.compiler.nodes.namespace.INameSpace;

/**
 * @author Julio Vilmar Gesser
 */
public class GenericTypeBinding extends Binding implements EncapsulatedBinding {

    private final String name;

    private final Compiler compiler;

    public GenericTypeBinding(Compiler compiler, String name) {
        this.compiler = compiler;
        this.name = name;
    }

    @Override
    public Binding getCommonType(Binding b) {
        return getType();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public INameSpace getNameSpace() {
        return getType().getNameSpace();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        return true;
    }

    public Binding getType() {
        return compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Object");
    }

}
