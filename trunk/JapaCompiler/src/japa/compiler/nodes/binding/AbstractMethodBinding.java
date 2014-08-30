/*
 * Created on 27/02/2007
 */
package japa.compiler.nodes.binding;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class AbstractMethodBinding extends MemberBinding implements EncapsulatedBinding {

    public abstract Binding[] getParameters();

    public abstract boolean isStatic();

}
