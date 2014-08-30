/*
 * Created on 25/03/2007
 */
package japa.compiler.nodes.binding;

import japa.compiler.nodes.namespace.INameSpace;

/**
 * @author Julio Vilmar Gesser
 */
public class VariableBinding extends Binding implements EncapsulatedBinding {

    private final String name;

    private final Binding type;

    public VariableBinding(String name, Binding type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Binding getCommonType(Binding b) {
        return type.getCommonType(b);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public INameSpace getNameSpace() {
        return type.getNameSpace();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        return type.isAssignableFrom(valueCB);
    }

    @Override
    public boolean isSpecialAssignableFrom(Binding valueCB) {
        return type.isSpecialAssignableFrom(valueCB);
    }

    public Binding getType() {
        return type;
    }

}
