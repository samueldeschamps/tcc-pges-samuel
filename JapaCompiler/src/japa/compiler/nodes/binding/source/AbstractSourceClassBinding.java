/*
 * Created on 06/05/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.Compiler;
import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.ArrayTypeBinding;
import japa.compiler.nodes.binding.Binding;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class AbstractSourceClassBinding extends AbstractClassBinding {

    protected final File javaFile;

    public AbstractSourceClassBinding(Compiler compiler, String id, String pkg, File javaFile) {
        super(compiler, id, pkg);
        this.javaFile = javaFile;
    }

    public abstract Binding getLocalMember(String name);

    protected final void addCompatibleMethods(String name, List<Binding> args, List<AbstractMethodBinding> methods, List<BodyDeclaration> members) {
        if (members != null) {
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    if (name.equals(method.getName()) && compareArgs(method.getParameters(), args)) {

                        AbstractMethodBinding methodBinding = getMethodBinding(method);
                        methods.add(methodBinding);
                        if (args == null) {
                            return;
                        }
                    }
                }
            }
        }

        for (Iterator<AbstractClassBinding> i = getBasesIterator(); i.hasNext();) {
            i.next().addCompatibleMethods(name, args, methods);
        }

    }

    protected final boolean compareArgs(List<Parameter> parameters, List<Binding> args) {
        if (parameters == null && args == null) {
            return true;
        }

        if (parameters != null && parameters.get(parameters.size() - 1).isVarArgs()) {
            if (args == null) {
                return true;
            }
            if (args.size() > parameters.size()) {
                Parameter last = parameters.get(parameters.size() - 1);
                parameters = new ArrayList<Parameter>(parameters);
                for (int i = parameters.size(); i < args.size(); i++) {
                    parameters.add(last);
                }
            }
        } else {
            if (parameters == null || args == null) {
                return false;
            }
            if (parameters.size() != args.size()) {
                return false;
            }
        }

        for (int i = 0; i < args.size(); i++) {
            Binding b = (Binding) parameters.get(i).getData();
            b = Binding.getInternalType(b);
            if (parameters.get(i).isVarArgs()) {
                if (internalCompareArgs(((ArrayTypeBinding) b).type, args.get(i))) {
                    continue;
                }
            }
            if (!internalCompareArgs(b, args.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected boolean internalCompareArgs(Binding l, Binding r) {
        return l.isAssignableFrom(r);
    }

    protected AbstractMethodBinding getMethodBinding(MethodDeclaration method) {
        return new SourceMethodBinding(method, this);
    }

    protected final Binding getLocalMember(String name, TypeDeclaration ltype, List<BodyDeclaration> members) {
        Binding ret = loadedMembers.get(name);
        if (ret != null) {
            return ret;
        }

        if (members != null) {
            for (BodyDeclaration member : members) {
                if (member instanceof FieldDeclaration) {
                    FieldDeclaration field = ((FieldDeclaration) member);
                    for (int i = 0; i < field.getVariables().size(); i++) {
                        VariableDeclarator var = field.getVariables().get(i);
                        if (var.getId().getName().equals(name)) {
                            ret = new SourceFieldBinding(field, i);
                            loadedMembers.put(name, ret);
                            return ret;
                        }
                    }
                }
                if (member instanceof TypeDeclaration) {
                    if (((TypeDeclaration) member).getName().equals(name)) {
                        ret = new SourceInnerClassBinding(this, name, (TypeDeclaration) member);
                        loadedMembers.put(name, ret);
                        return ret;
                    }
                }
            }
        }
        if (ltype instanceof EnumDeclaration) {
            EnumDeclaration enumDec = (EnumDeclaration) ltype;
            if (enumDec.getEntries() != null) {
                for (EnumConstantDeclaration item : enumDec.getEntries()) {
                    if (item.getName().equals(name)) {
                        ret = new SourceEnumItemBinding(this, item);
                        loadedMembers.put(name, ret);
                        return ret;
                    }
                }
            }
        }
        return null;
    }

}
