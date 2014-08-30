/*
 * Created on 15/01/2007
 */
package japa.compiler.nodes.namespace;

import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.compiler.nodes.binding.PackageBinding;
import japa.parser.ast.expr.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class ClassNameSpace implements INameSpace {

    private final AbstractClassBinding ce;

    private final boolean isStatic;

    private final boolean isAsterisk;

    public ClassNameSpace(AbstractClassBinding ce, boolean isStatic, boolean isAsterisk) {
        this.ce = ce;
        this.isStatic = isStatic;
        this.isAsterisk = isAsterisk;
    }

    public Binding resolve(String id) {
        if (!isAsterisk) {
            if (id.equals(ce.id)) {
                return ce;
            }
        } else {
            return ce.getMember(id);
        }
        return null;
    }

    public MemberBinding resolve(String id, List<Expression> args) throws AmbiguousMethodException {
        List<Binding> list = null;
        if (args != null) {
            list = new ArrayList<Binding>();
            for (Expression e : args) {
                list.add(Binding.getInternalType((Binding) e.getData()));
            }
        }
        if (id.equals("")) {
            return ce.getConstructor(list);
        }
        return ce.getMethod(id, list);
    }

    public Binding resolveClass(String id) {
        Binding ret = resolve(id);
        if (ret instanceof AbstractClassBinding || ret instanceof PackageBinding) {
            return ret;
        }
        return null;
    }
}
