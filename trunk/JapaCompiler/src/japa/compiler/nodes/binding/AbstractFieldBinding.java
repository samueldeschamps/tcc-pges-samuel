/*
 * Created on 31/01/2007
 */
package japa.compiler.nodes.binding;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class AbstractFieldBinding extends MemberBinding implements EncapsulatedBinding {

    public abstract boolean isStatic();

}
