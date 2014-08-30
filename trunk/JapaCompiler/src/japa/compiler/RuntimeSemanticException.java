/*
 * Created on 11/12/2006
 */
package japa.compiler;

import japa.parser.ast.Node;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author Julio Vilmar Gesser
 */
public class RuntimeSemanticException extends RuntimeException {

    public RuntimeSemanticException(Throwable cause) {
        super(cause);
    }

    public RuntimeSemanticException(String message, Object[] args, Node node, File file) {
        this(new MessageFormat(message).format(args), node, file);
    }

    public RuntimeSemanticException(String message, Node node, File file) {
        this(message, node.getBeginLine(), node.getBeginColumn(), file);
    }

    public RuntimeSemanticException(String message, Object[] args, int line, int column, File file) {
        this(new MessageFormat(message).format(args), line, column, file);
    }

    public RuntimeSemanticException(String message, int line, int column, File file) {
        super(file.getPath() + ": " + message + " at line " + line + ", column " + column + ".");
    }

}
