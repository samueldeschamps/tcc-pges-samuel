/*
 * Created on 24/02/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.Compiler;
import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Julio Vilmar Gesser
 */
public class BinaryMethodBinding extends AbstractMethodBinding {

    public final Method method;

    private final Compiler compiler;

    private Binding[] parameters;

    public BinaryMethodBinding(Compiler compiler, Method method) {
        this.compiler = compiler;
        this.method = method;
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
        return BinaryClassBinding.classToType(compiler, method.getReturnType());
    }

    @Override
    public Binding getCommonType(Binding b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Binding[] getParameters() {
        if (parameters == null) {
            Class[] parameterTypes = method.getParameterTypes();
            parameters = new Binding[parameterTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = BinaryClassBinding.classToType(compiler, parameterTypes[i]);
            }
        }
        return parameters;
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

}
