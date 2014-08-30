/*
 * Created on 18/01/2007
 */
package japa.compiler;

import japa.compiler.nodes.binding.GenericTypeBinding;
import japa.compiler.nodes.binding.source.AbstractSourceClassBinding;
import japa.parser.ast.Node;
import japa.parser.ast.body.TypeDeclaration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
final class BlockScope {

    private final Map<String, Node> symbols;

    private final Map<String, TypeDeclaration> types;

    private final Map<String, GenericTypeBinding> generics;

    private AbstractSourceClassBinding classBinding;

    public BlockScope() {
        this.symbols = new HashMap<String, Node>();
        this.types = new HashMap<String, TypeDeclaration>();
        this.generics = new HashMap<String, GenericTypeBinding>();
    }

    public void addSymbol(String id, Node symbol) {
        assert !symbols.containsKey(id);
        symbols.put(id, symbol);
    }

    public Node getSymbol(String id) {
        return symbols.get(id);
    }

    public boolean containsSymbol(String id) {
        return symbols.containsKey(id);
    }

    public void addType(String id, TypeDeclaration type) {
        assert !types.containsKey(id);
        types.put(id, type);
    }

    public TypeDeclaration getType(String id) {
        return types.get(id);
    }

    public boolean containsType(String id) {
        return types.containsKey(id);
    }

    public void setClassBinding(AbstractSourceClassBinding classBinding) {
        this.classBinding = classBinding;
    }

    public AbstractSourceClassBinding getClassBinding() {
        return classBinding;
    }

    public void addGenericType(String name, GenericTypeBinding b) {
        generics.put(name, b);
    }

    public GenericTypeBinding getGenericType(String name) {
        return generics.get(name);
    }

}