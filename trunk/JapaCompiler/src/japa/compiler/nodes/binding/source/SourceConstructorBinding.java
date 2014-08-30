/*
 * Created on 24/02/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.nodes.binding.AbstractConstructorBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.body.ConstructorDeclaration;

/**
 * @author Julio Vilmar Gesser
 */
public class SourceConstructorBinding extends AbstractConstructorBinding {

    private final ConstructorDeclaration constructor;

    private Binding[] parameters;

    public SourceConstructorBinding(ConstructorDeclaration constructor) {
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
            if (constructor.getParameters() == null) {
                parameters = new Binding[0];
            } else {
                parameters = new Binding[constructor.getParameters().size()];
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = (Binding) constructor.getParameters().get(i).getData();
                }
            }
        }
        return parameters;
    }

}
