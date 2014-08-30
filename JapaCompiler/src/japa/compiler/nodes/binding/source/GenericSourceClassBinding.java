/*
 * Created on 05/03/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.GenericTypeBinding;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
public class GenericSourceClassBinding extends SourceClassBinding {

    private final Map<String, Binding> types;

    public GenericSourceClassBinding(SourceClassBinding inner, List<Binding> types) {
        super(inner.compiler, inner.id, inner.pkg, inner.javaFile);

        this.types = new HashMap<String, Binding>();
        ClassOrInterfaceDeclaration t = (ClassOrInterfaceDeclaration) getType();
        int i = 0;
        for (TypeParameter p : t.getTypeParameters()) {
            this.types.put(p.getName(), types.get(i++));
        }
    }

    @Override
    public AbstractMethodBinding getMethodBinding(MethodDeclaration method) {
        return new GenericSourceMethodBinding(this, super.getMethodBinding(method));
    }

    @Override
    protected boolean internalCompareArgs(Binding l, Binding r) {
        return super.internalCompareArgs(getMapped(l), r);
    }

    protected final Binding getMapped(Binding b) {
        if (b instanceof GenericTypeBinding) {
            GenericTypeBinding g = (GenericTypeBinding) b;
            return types.get(g.getName());
        }
        return b;
    }
}
