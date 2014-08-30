/*
 * Created on 07/01/2007
 */
package japa.compiler;

import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.GenericTypeBinding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.compiler.nodes.binding.source.AbstractSourceClassBinding;
import japa.compiler.nodes.namespace.INameSpace;
import japa.compiler.util.Stack;
import japa.parser.ast.Node;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.Expression;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class CompilingScope implements INameSpace {

    private final Stack<BlockScope> stack;

    private String pkg = "";

    public CompilingScope() {
        this.stack = new Stack<BlockScope>();
        openBlock(); // CompilationUnit top level types
    }

    public void openBlock() {
        stack.push(new BlockScope());
    }

    public void closeBlock() {
        stack.pop();
    }

    public void addSymbol(String id, Node symbol) {
        stack.peek().addSymbol(id, symbol);
    }

    public Node getSymbol(String id) {
        Node ret = null;
        int i = stack.size();
        while (ret == null && i > 0) {
            ret = stack.peek(--i).getSymbol(id);
        }
        return ret;
    }

    public boolean containsSymbol(String id) {
        boolean ret = false;
        int i = stack.size();
        while (!ret && i > 0) {
            ret = stack.peek(--i).containsSymbol(id);
        }
        return ret;
    }

    public boolean containsSymbolInScope(String id) {
        return stack.peek().containsSymbol(id);
    }

    public void addType(String id, TypeDeclaration type) {
        stack.peek().addType(id, type);
    }

    public TypeDeclaration getType(String id) {
        TypeDeclaration ret = null;
        int i = stack.size();
        while (ret == null && i > 0) {
            ret = stack.peek(--i).getType(id);
        }
        return ret;
    }

    public boolean containsTypeInScope(String id) {
        return stack.peek().containsType(id);
    }

    public void setClassBinding(AbstractSourceClassBinding classBinding) {
        stack.peek().setClassBinding(classBinding);
    }

    public AbstractSourceClassBinding getClassBinding() {
        return stack.peek().getClassBinding();
    }

    public AbstractSourceClassBinding getParentClassBinding() {
        AbstractSourceClassBinding ret = null;
        int i = stack.size();
        while (ret == null && i > 0) {
            ret = stack.peek(--i).getClassBinding();
        }
        return ret;
    }

    public void addGenericType(String name, GenericTypeBinding b) {
        stack.peek().addGenericType(name, b);
    }

    public GenericTypeBinding getGenericType(String name) {
        GenericTypeBinding ret = null;
        int i = stack.size();
        while (ret == null && i > 0) {
            ret = stack.peek(--i).getGenericType(name);
        }
        return ret;
    }

    public Binding resolve(String id) {
        Node n = getSymbol(id);
        if (n != null) {
            return (Binding) n.getData();
        }
        TypeDeclaration t = getType(id);
        if (t != null) {
            return (Binding) t.getData();
        }
        GenericTypeBinding g = getGenericType(id);
        if (g != null) {
            return g;
        }
        return null;
    }

    public MemberBinding resolve(String id, List<Expression> args) {
        return null;
    }

    public Binding resolveClass(String id) {
        TypeDeclaration t = getType(id);
        if (t != null) {
            return (Binding) t.getData();
        }
        GenericTypeBinding g = getGenericType(id);
        if (g != null) {
            return g;
        }
        return null;
    }

    public void setPackage(String pkg) {
        this.pkg = pkg;
    }

    public String getPackage() {
        return pkg;
    }

}
