/*
 * Created on 12/03/2007
 */
package japa.compiler.nodes.binding;

import japa.compiler.nodes.namespace.INameSpace;

/**
 * @author Julio Vilmar Gesser
 */
public final class WildcardTypeBinding extends Binding {

    @Override
    public Binding getCommonType(Binding b) {
        return b;
    }

    @Override
    public String getName() {
        return "?";
    }

    @Override
    public INameSpace getNameSpace() {
        return INameSpace.EMPTY_NAME_SPACE;
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        return false;
    }

}
