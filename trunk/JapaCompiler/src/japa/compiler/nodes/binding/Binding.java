/*
 * Created on 12/12/2006
 */
package japa.compiler.nodes.binding;

import japa.compiler.nodes.namespace.INameSpace;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class Binding {

    /**
     * Retorna um resolvedor de nomes para este tipo.
     */
    public abstract INameSpace getNameSpace();

    /**
     * Retorna o nome deste tipo
     */
    public abstract String getName();

    /**
     * Veriica se uma variavel do tipo de parametro pode ser atribuida a uma variavel deste tipo. 
     * @param valueCB tipo
     * @return true se a atribuição for possível
     */
    public abstract boolean isAssignableFrom(Binding valueCB);

    /**
     * Verifica se uma variavel do tipo de parametro pode ser atribuida a uma variavel deste tipo,
     * considerando literais e autoboxing.
     * @param valueCB tipo
     * @return true se a atribuição for possível
     */
    public boolean isSpecialAssignableFrom(Binding valueCB) {
        return isAssignableFrom(valueCB);
    }

    @Override
    public final String toString() {
        return getName() + " : " + this.getClass().getName();
    }

    /**
     * Retorna o tipo comum entre este tipo e o parametro.
     */
    public abstract Binding getCommonType(Binding b);

    /**
     * Retorna o tipo com os argumentos genericos tipados.
     */
    public Binding getGenericType(List<Binding> types) {
        assert types == types;
        return null;
    }

    public static Binding getInternalType(Binding b) {
        if (b instanceof EncapsulatedBinding) {
            return getInternalType(((EncapsulatedBinding) b).getType());
        }
        return b;
    }
}
