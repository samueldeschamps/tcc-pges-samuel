/*
 * Created on 11/12/2006
 */
package japa.compiler.nodes.namespace;

import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.parser.ast.expr.Expression;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class NamesContext implements INameSpace {

    private final NamesContext parent;

    private final INameSpace ns;

    public NamesContext(INameSpace ns, NamesContext parent) {
        this.ns = ns;
        this.parent = parent;
    }

    public NamesContext(INameSpace ns) {
        this(ns, null);
    }

    public NamesContext push(INameSpace newNs) {
        return new NamesContext(newNs, this);
    }

    public NamesContext pop() {
        return parent;
    }

    public Binding resolve(String id) {
        Binding ret = ns.resolve(id);
        if (ret != null) {
            return ret;
        }
        if (parent == null) {
            return null;
        }
        return parent.resolve(id);
    }

    public MemberBinding resolve(String id, List<Expression> args) throws AmbiguousMethodException {
        MemberBinding ret = ns.resolve(id, args);
        if (ret != null) {
            return ret;
        }
        if (parent == null) {
            return null;
        }
        return parent.resolve(id, args);
    }

    public Binding resolveClass(String id) {
        Binding ret = ns.resolveClass(id);
        if (ret != null) {
            return ret;
        }
        if (parent == null) {
            return null;
        }
        return parent.resolveClass(id);
    }

}
