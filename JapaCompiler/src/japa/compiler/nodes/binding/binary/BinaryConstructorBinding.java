/*
 * Created on 24/02/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.Compiler;
import japa.compiler.nodes.binding.AbstractConstructorBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;

import java.lang.reflect.Constructor;

/**
 * @author Julio Vilmar Gesser
 */
public class BinaryConstructorBinding extends AbstractConstructorBinding {

    private final Constructor constructor;

    private final Compiler compiler;

    private Binding[] parameters;

    public BinaryConstructorBinding(Compiler compiler, Constructor constructor) {
        this.compiler = compiler;
        this.constructor = constructor;
    }

    @Override
    public INameSpace getNameSpace() {
        return INameSpace.EMPTY_NAME_SPACE;
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Binding getCommonType(Binding b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Binding[] getParameters() {
        if (parameters == null) {
            Class[] parameterTypes = constructor.getParameterTypes();
            parameters = new Binding[parameterTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = BinaryClassBinding.classToType(compiler, parameterTypes[i]);
            }
        }
        return parameters;
    }
}
