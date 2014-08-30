/*
 * Created on 24/02/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.nodes.binding.AbstractFieldBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.body.EnumConstantDeclaration;

/**
 * @author Julio Vilmar Gesser
 */
public class SourceEnumItemBinding extends AbstractFieldBinding {

    private final Binding parent;

    private final EnumConstantDeclaration item;

    public SourceEnumItemBinding(Binding parent, EnumConstantDeclaration item) {
        this.parent = parent;
        this.item = item;
    }

    public Binding getType() {
        return parent;
    }

    @Override
    public String getName() {
        return item.getName();
    }

    @Override
    public INameSpace getNameSpace() {
        return INameSpace.EMPTY_NAME_SPACE;
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
    public boolean isStatic() {
        return true;
    }

}
