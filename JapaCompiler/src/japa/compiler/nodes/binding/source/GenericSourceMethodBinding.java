/*
 * Created on 06/03/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;

/**
 * @author Julio Vilmar Gesser
 */
public class GenericSourceMethodBinding extends AbstractMethodBinding {

    private final GenericSourceClassBinding owner;

    private final AbstractMethodBinding method;

    private Binding[] parameters;

    public GenericSourceMethodBinding(GenericSourceClassBinding owner, AbstractMethodBinding method) {
        this.owner = owner;
        this.method = method;
    }

    public Binding getType() {
        return owner.getMapped(method.getType());
    }

    @Override
    public Binding getCommonType(Binding b) {
        return method.getCommonType(b);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public INameSpace getNameSpace() {
        return getType().getNameSpace();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        return false;
    }

    @Override
    public Binding[] getParameters() {
        if (parameters == null) {
            Binding[] args = method.getParameters();
            parameters = new Binding[args.length];
            for (int i = 0; i < args.length; i++) {
                parameters[i] = owner.getMapped(args[i]);
            }
        }
        return parameters;
    }

    @Override
    public boolean isStatic() {
        return method.isStatic();
    }

}
