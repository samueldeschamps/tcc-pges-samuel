/*
 * Created on 12/12/2006
 */
package japa.compiler.nodes.binding;

import japa.compiler.nodes.namespace.AmbiguousMethodException;
import japa.compiler.nodes.namespace.ClassNameSpace;
import japa.compiler.nodes.namespace.INameSpace;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class AbstractClassBinding extends Binding {

    public final String id;

    public final String pkg;

    public final japa.compiler.Compiler compiler;

    protected final Map<String, Binding> loadedMembers;

    public AbstractClassBinding(japa.compiler.Compiler compiler, String id, String pkg) {
        this.compiler = compiler;
        this.id = id;
        this.pkg = pkg;
        this.loadedMembers = new HashMap<String, Binding>();
    }

    /**
     * Pega o full id da classe referenciada (pacote.Classe)
     * @return
     */
    @Override
    public String getName() {
        if (pkg.length() > 0) {
            return pkg + "." + id;
        }
        return id;
    }

    /**
     * Retorna o nome da classe, sem o pacote.
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome do pacote.
     * @return
     */
    public String getPackage() {
        return pkg;
    }

    @Override
    public final INameSpace getNameSpace() {
        return new ClassNameSpace(this, false, true);
    }

    @Override
    public final boolean isAssignableFrom(Binding valueCB) {
        if (getName().equals("java.lang.Object")) {
            return true;
        }

        valueCB = getInternalType(valueCB);
        if (valueCB == PrimitiveTypeBinding.NullType) {
            return true;
        }

        if (valueCB instanceof PrimitiveTypeBinding) {
            PrimitiveTypeBinding unboxing = PrimitiveTypeBinding.getUnboxing(getName());
            if (unboxing != null) {
                return unboxing.isAssignableFrom(valueCB);
            }
            valueCB = PrimitiveTypeBinding.getBoxing(compiler, (PrimitiveTypeBinding) valueCB);
        }

        if (!(valueCB instanceof AbstractClassBinding)) {
            return false;
        }
        if (equals(valueCB)) {
            return true;
        }
        return ((AbstractClassBinding) valueCB).isIntanceOf(this);
    }

    @Override
    public boolean isSpecialAssignableFrom(Binding valueCB) {
        valueCB = getInternalType(valueCB);
        if (valueCB == PrimitiveTypeBinding.NullType) {
            return true;
        }
        if (valueCB instanceof PrimitiveTypeBinding) {
            PrimitiveTypeBinding unboxing = PrimitiveTypeBinding.getUnboxing(getName());
            if (unboxing != null) {
                return unboxing.isSpecialAssignableFrom(valueCB);
            }
        }
        if ((valueCB instanceof AbstractClassBinding)) {
            if (((AbstractClassBinding) valueCB).isInterface()) {
                return true;
            }
        }
        return super.isSpecialAssignableFrom(valueCB);
    }

    protected final String getMethodKey(String name, List<Binding> args) {
        StringBuilder buf = new StringBuilder();
        buf.append(name);
        buf.append("(");
        if (args != null) {
            for (Binding b : args) {
                buf.append(b.getName());
                buf.append(",");
            }
        }
        String key = buf.toString();
        return key;
    }

    /**
     * Retorna um método com os parametros especificados, ou nulo se não encontrar.
     * @param name nome do metodo
     * @param types tipos dos parametros do construtor
     * @throws AmbiguousMethodException 
     */
    public final AbstractMethodBinding getMethod(String name, List<Binding> args) throws AmbiguousMethodException {
        String key = getMethodKey(name, args);
        AbstractMethodBinding ret = (AbstractMethodBinding) loadedMembers.get(key);
        if (ret != null) {
            return ret;
        }

        List<AbstractMethodBinding> methods = new LinkedList<AbstractMethodBinding>();
        addCompatibleMethods(name, args, methods);

        if (methods.size() == 0) {
            return null;
        }

        ret = getMoreSpecificMethod(methods);
        loadedMembers.put(key, ret);
        return ret;
    }

    protected final AbstractMethodBinding getMoreSpecificMethod(List<AbstractMethodBinding> methods) throws AmbiguousMethodException {
        final boolean[] ambiguous = new boolean[1];

        if (methods.size() > 1) {
            Collections.sort(methods, new Comparator<AbstractMethodBinding>() {

                public int compare(AbstractMethodBinding m1, AbstractMethodBinding m2) {
                    int ret = 0;
                    Binding[] p1 = m1.getParameters();
                    Binding[] p2 = m2.getParameters();
                    for (int i = 0; i < p1.length; i++) {
                        Binding b1 = Binding.getInternalType(p1[i]);
                        Binding b2 = Binding.getInternalType(p2[i]);
                        if (b1.equals(b2)) {
                            continue;
                        }
                        if (b1.isAssignableFrom(b2)) {
                            if (ret == -1) {
                                ambiguous[0] = true;
                                return 0;
                            }
                            ret = 1;
                        } else if (b2.isAssignableFrom(b1)) {
                            if (ret == 1) {
                                ambiguous[0] = true;
                                return 0;
                            }
                            ret = -1;
                        }
                    }
                    if (ret == 0) {
                        if (m1.getType().isAssignableFrom(m2.getType())) {
                            return 1;
                        }
                        if (m2.getType().isAssignableFrom(m1.getType())) {
                            return -1;
                        }
                    }
                    return ret;
                }

            });
        }
        if (ambiguous[0]) {
            throw new AmbiguousMethodException();
        }
        return methods.get(0);
    }

    /**
     * Retorna um construtor com os parametros especificados, ou nulo se não encontrar.
     * @param types tipos dos parametros do construtor
     * @throws AmbiguousMethodException 
     */
    public final AbstractConstructorBinding getConstructor(List<Binding> args) throws AmbiguousMethodException {
        String key = getMethodKey("", args);
        AbstractConstructorBinding ret = (AbstractConstructorBinding) loadedMembers.get(key);
        if (ret != null) {
            return ret;
        }

        List<AbstractConstructorBinding> constructors = new LinkedList<AbstractConstructorBinding>();
        addCompatibleConstructors(args, constructors);

        if (constructors.size() == 0) {
            return null;
        }

        ret = getMoreSpecificConstructor(constructors);
        loadedMembers.put(key, ret);
        return ret;
    }

    protected final AbstractConstructorBinding getMoreSpecificConstructor(List<AbstractConstructorBinding> constructors) throws AmbiguousMethodException {
        final boolean[] ambiguous = new boolean[1];

        if (constructors.size() > 1) {
            Collections.sort(constructors, new Comparator<AbstractConstructorBinding>() {

                public int compare(AbstractConstructorBinding m1, AbstractConstructorBinding m2) {
                    int ret = 0;
                    Binding[] p1 = m1.getParameters();
                    Binding[] p2 = m2.getParameters();
                    for (int i = 0; i < p1.length; i++) {
                        Binding b1 = Binding.getInternalType(p1[i]);
                        Binding b2 = Binding.getInternalType(p2[i]);
                        if (b1.equals(b2)) {
                            continue;
                        }
                        if (b1.isAssignableFrom(b2)) {
                            if (ret == -1) {
                                ambiguous[0] = true;
                                return 0;
                            }
                            ret = 1;
                        } else if (b2.isAssignableFrom(b1)) {
                            if (ret == 1) {
                                ambiguous[0] = true;
                                return 0;
                            }
                            ret = -1;
                        }
                    }
                    return ret;
                }

            });
        }
        if (ambiguous[0]) {
            throw new AmbiguousMethodException();
        }
        return constructors.get(0);
    }

    /**
     * Verifica se um objeto do tipo de parametro é uma instancia deste tipo.
     */
    public abstract boolean isIntanceOf(AbstractClassBinding valueCB);

    /**
     * Verifica se o tipo é uma interface
     */
    public abstract boolean isInterface();

    /**
     * Verifica se o tipo é uma enumeração
     */
    public abstract boolean isEnum();

    /**
     * Retorna a os modificdores do tipo
     */
    public abstract int getModifiers();

    /**
     * Retorna o binding de um membro da classe, ou null se não encontrar.
     * @return
     */
    public abstract Binding getMember(String name);

    /**
     * Enumera todos os métodos com o nome e numero de parametros especificado.
     * @param name nome do metodo
     * @param numArgs numero de parametros
     * @param methods lista que recebera os metodos
     */
    public abstract void addCompatibleMethods(String name, List<Binding> args, List<AbstractMethodBinding> methods);

    /**
     * Enumera todos os construtores com o numero de parametros especificados.
     * @param numArgs numero de parametros
     * @param constructors lista que recebera os construtores
     */
    public abstract void addCompatibleConstructors(List<Binding> args, List<AbstractConstructorBinding> constructors);

    /**
     * Retorna iterador para suas classes bases e interfaces diretas e indiretas.
     * @return
     */
    public abstract Iterator<AbstractClassBinding> getBasesIterator();

    /**
     * Retorna se o tipo possui contrutores declarados. 
     */
    public abstract boolean hasConstructors();

}
