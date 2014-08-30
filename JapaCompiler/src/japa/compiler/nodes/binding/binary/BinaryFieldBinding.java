/*
 * Created on 30/01/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.Compiler;
import japa.compiler.nodes.binding.AbstractFieldBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Julio Vilmar Gesser
 */
public class BinaryFieldBinding extends AbstractFieldBinding {

    private final Field field;

    private final Compiler compiler;

    public BinaryFieldBinding(Compiler compiler, Field field) {
        this.compiler = compiler;
        this.field = field;
    }

    @Override
    public INameSpace getNameSpace() {
        return getType().getNameSpace();
    }

    @Override
    public String getName() {
        return getType().getName();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        // TODO Auto-generated method stub
        return false;
    }

    public Binding getType() {
        return BinaryClassBinding.classToType(compiler, field.getType());
    }

    @Override
    public Binding getCommonType(Binding b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }
}
