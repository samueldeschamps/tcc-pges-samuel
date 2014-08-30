/*
 * Created on 06/03/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.nodes.binding.Binding;

/**
 * @author Julio Vilmar Gesser
 */
public class GenericBinaryMethodBinding extends BinaryMethodBinding {

    private final GenericBinaryClassBinding owner;

    public GenericBinaryMethodBinding(GenericBinaryClassBinding owner, BinaryMethodBinding method) {
        super(owner.compiler, method.method);
        this.owner = owner;
    }

    @Override
    public Binding getType() {
        return owner.getBinding(method.getGenericReturnType());
    }

}
