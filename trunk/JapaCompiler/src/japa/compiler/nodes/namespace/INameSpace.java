/*
 * Created on 10/12/2006
 */
package japa.compiler.nodes.namespace;

import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.parser.ast.expr.Expression;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public interface INameSpace {

    INameSpace EMPTY_NAME_SPACE = new INameSpace() {

        public Binding resolve(String id) {
            return null;
        }

        public MemberBinding resolve(String id, List<Expression> args) {
            return null;
        }

        public Binding resolveClass(String id) {
            return null;
        }

    };

    Binding resolve(String id);

    MemberBinding resolve(String id, List<Expression> args) throws AmbiguousMethodException;

    Binding resolveClass(String id);

}
