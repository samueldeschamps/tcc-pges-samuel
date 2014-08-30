/*
 * Created on 24/01/2007
 */
package japa.compiler.nodes.binding.source;

import japa.parser.ast.body.TypeDeclaration;

/**
 * @author Julio Vilmar Gesser
 */
public final class SourceInnerClassBinding extends SourceClassBinding {

    public SourceInnerClassBinding(AbstractSourceClassBinding parent, String id, TypeDeclaration type) {
        super(parent.compiler, parent.getId() + "_" + id, parent.getPackage(), parent.javaFile, type);
    }
}
