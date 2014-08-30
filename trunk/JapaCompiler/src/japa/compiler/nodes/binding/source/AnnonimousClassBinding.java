/*
 * Created on 06/05/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.Compiler;
import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.AbstractConstructorBinding;
import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.Binding;
import japa.parser.ast.body.BodyDeclaration;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class AnnonimousClassBinding extends AbstractSourceClassBinding {

    private final AbstractClassBinding type;

    private final List<BodyDeclaration> members;

    public AnnonimousClassBinding(Compiler compiler, AbstractClassBinding abstractClassBinding, List<BodyDeclaration> members, File javaFile) {
        super(compiler, "", "", javaFile);
        this.type = abstractClassBinding;
        this.members = members;
    }

    @Override
    public void addCompatibleConstructors(List<Binding> args, List<AbstractConstructorBinding> constructors) {
        type.addCompatibleConstructors(args, constructors);
    }

    @Override
    public void addCompatibleMethods(String name, List<Binding> args, List<AbstractMethodBinding> methods) {
        addCompatibleMethods(name, args, methods, members);
        type.addCompatibleMethods(name, args, methods);
    }

    @Override
    public Iterator<AbstractClassBinding> getBasesIterator() {
        return type.getBasesIterator();
    }

    @Override
    public Binding getLocalMember(String name) {
        return getLocalMember(name, null, members);
    }

    @Override
    public Binding getMember(String name) {
        Binding ret = getLocalMember(name);
        if (ret != null) {
            return ret;
        }
        return type.getMember(name);
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public boolean hasConstructors() {
        return type.hasConstructors();
    }

    @Override
    public boolean isEnum() {
        return type.isEnum();
    }

    @Override
    public boolean isIntanceOf(AbstractClassBinding valueCB) {
        return type.isIntanceOf(valueCB);
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public Binding getCommonType(Binding b) {
        return type.getCommonType(b);
    }

    @Override
    public String getName() {
        return type.getName();
    }

}
