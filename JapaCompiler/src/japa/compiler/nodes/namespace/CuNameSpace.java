/*
 * Created on 08/01/2007
 */
package japa.compiler.nodes.namespace;

import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.compiler.nodes.binding.source.SourceClassBinding;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.Expression;

import java.io.File;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class CuNameSpace implements INameSpace {

    private final CompilationUnit cu;

    private final japa.compiler.Compiler compiler;

    private final File file;

    public CuNameSpace(japa.compiler.Compiler compiler, CompilationUnit cu, File file) {
        this.compiler = compiler;
        this.cu = cu;
        this.file = file;
    }

    public Binding resolve(String id) {
        for (TypeDeclaration type : cu.getTypes()) {
            if (type.getName().equals(id)) {
                return new SourceClassBinding(compiler, id, cu.getPackage() == null ? "" : cu.getPackage().getName().toString(), file);
            }
        }
        return null;
    }

    public MemberBinding resolve(String id, List<Expression> args) {
        return null;
    }

    public Binding resolveClass(String id) {
        return resolve(id);
    }
}
