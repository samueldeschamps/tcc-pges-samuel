/*
 * Created on 14/12/2006
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;

/**
 * @author Julio Vilmar Gesser
 */
public class SourceMethodBinding extends AbstractMethodBinding {

    protected final MethodDeclaration methodDeclaration;
    private AbstractSourceClassBinding sourceClassBinding;

    private Binding[] parameters;

    public SourceMethodBinding(MethodDeclaration methodDeclaration, AbstractSourceClassBinding sourceClassBinding) {
        this.methodDeclaration = methodDeclaration;
        this.sourceClassBinding = sourceClassBinding;
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
        return getType().isAssignableFrom(valueCB);
    }

    @Override
    public Binding getCommonType(Binding b) {
        return getType().getCommonType(b);
    }

    public Binding getType() {
        return (Binding) methodDeclaration.getType().getData();
    }

    @Override
    public Binding[] getParameters() {
        if (parameters == null) {
            if (methodDeclaration.getParameters() == null) {
                parameters = new Binding[0];
            } else {
                parameters = new Binding[methodDeclaration.getParameters().size()];
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = (Binding) methodDeclaration.getParameters().get(i).getData();
                }
            }
        }
        return parameters;
    }

    @Override
    public boolean isStatic() {
        return ModifierSet.isStatic(methodDeclaration.getModifiers());
    }
    
    public AbstractSourceClassBinding getSourceClassBinding() {
		return sourceClassBinding;
	}
    
    public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

}
