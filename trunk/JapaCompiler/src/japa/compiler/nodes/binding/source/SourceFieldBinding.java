/*
 * Created on 31/01/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.nodes.binding.AbstractFieldBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.ModifierSet;

/**
 * @author Julio Vilmar Gesser
 */
public class SourceFieldBinding extends AbstractFieldBinding {

    private final FieldDeclaration field;

    private final int varIndex;

    private final Binding type;

    public SourceFieldBinding(FieldDeclaration field, int varIndex) {
        this.field = field;
        this.varIndex = varIndex;
        this.type = null;
    }

    public SourceFieldBinding(FieldDeclaration field, int varIndex, Binding type) {
        this.field = field;
        this.varIndex = varIndex;
        this.type = type;
    }

    public Binding getType() {
        if (type != null) {
            return type;
        }
        return (Binding) field.getVariables().get(varIndex).getData();
    }

    @Override
    public String getName() {
        return getType().getName();
    }

    @Override
    public INameSpace getNameSpace() {
        return getType().getNameSpace();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        return getType().isAssignableFrom(valueCB);
    }

    @Override
    public Binding getCommonType(Binding b) {
        return getType().getCommonType(b);
    }

    @Override
    public boolean isStatic() {
        return ModifierSet.isStatic(field.getModifiers());
    }

}
