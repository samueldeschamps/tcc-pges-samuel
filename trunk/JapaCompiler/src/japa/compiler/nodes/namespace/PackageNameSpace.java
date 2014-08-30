/*
 * Created on 15/01/2007
 */
package japa.compiler.nodes.namespace;

import japa.compiler.FileManager;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.parser.ast.expr.Expression;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class PackageNameSpace implements INameSpace {

    private final FileManager fileManager;

    private final String pkg;

    public PackageNameSpace(FileManager fileManager, String pkg) {
        this.fileManager = fileManager;
        this.pkg = pkg;
    }

    public Binding resolve(String id) {
        return fileManager.getBinding(pkg, id);
    }

    public MemberBinding resolve(String id, List<Expression> args) {
        return null;
    }

    public Binding resolveClass(String id) {
        return resolve(id);
    }

}
