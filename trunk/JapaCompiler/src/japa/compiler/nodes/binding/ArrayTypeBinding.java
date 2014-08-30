/*
 * Created on 30/01/2007
 */
package japa.compiler.nodes.binding;

import japa.compiler.FileManager;
import japa.compiler.nodes.namespace.AmbiguousMethodException;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.expr.Expression;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class ArrayTypeBinding extends Binding {

    public final japa.compiler.Compiler compiler;

    public final Binding type;

    public final int arrayCount;

    public ArrayTypeBinding(japa.compiler.Compiler compiler, Binding type, int arrayCount) {
        if (type instanceof ArrayTypeBinding) {
            arrayCount += ((ArrayTypeBinding) type).arrayCount;
            type = ((ArrayTypeBinding) type).type;
        }
        this.type = type;
        this.arrayCount = arrayCount;
        this.compiler = compiler;
    }

    @Override
    public String getName() {
        StringBuffer ret = new StringBuffer();
        if (type == null) {
            ret.append("null");
        } else {
            ret.append(type.getName());
        }
        for (int i = 0; i < arrayCount; i++) {
            ret.append("[]");
        }
        return ret.toString();
    }

    @Override
    public INameSpace getNameSpace() {

        return new INameSpace() {

            final INameSpace objNS = compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Object").getNameSpace();

            public Binding resolve(String id) {
                if (id.equals("length")) {
                    return new LengthFieldBinding();
                }
                return objNS.resolve(id);
            }

            public MemberBinding resolve(String id, List<Expression> args) throws AmbiguousMethodException {
                if (id.equals("clone") && args == null) {
                    return new CloneMethodBinding(ArrayTypeBinding.this);
                }
                return objNS.resolve(id, args);
            }

            public Binding resolveClass(String id) {
                return objNS.resolveClass(id);
            }
        };
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        valueCB = getInternalType(valueCB);
        if (valueCB instanceof ArrayTypeBinding) {
            ArrayTypeBinding arrayTypeBinding = (ArrayTypeBinding) valueCB;
            return arrayTypeBinding.type == null //
                    || (type.isAssignableFrom(arrayTypeBinding.type) && arrayCount == arrayTypeBinding.arrayCount);
        }
        if (valueCB instanceof PrimitiveTypeBinding) {
            PrimitiveTypeBinding binding = (PrimitiveTypeBinding) valueCB;
            if (binding.type == null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Binding getCommonType(Binding b) {
        if (b instanceof AbstractFieldBinding) {
            b = ((EncapsulatedBinding) b).getType();
        }

        if (b == PrimitiveTypeBinding.NullType) {
            return this;
        }

        if (b instanceof ArrayTypeBinding) {
            ArrayTypeBinding at = (ArrayTypeBinding) b;
            if (at.type == null) { // array vazio
                return this;
            }
            if (type == null) { // array vazio
                return b;
            }
            if (arrayCount == at.arrayCount) {
                return new ArrayTypeBinding(compiler, type.getCommonType(at.type), arrayCount);
            }
        }

        return null;
    }

    /**
     * @author jgesser
     */
    private static final class LengthFieldBinding extends AbstractFieldBinding {

        @Override
        public Binding getCommonType(Binding b) {
            return getType().getCommonType(b);
        }

        @Override
        public String getName() {
            return "length";
        }

        @Override
        public INameSpace getNameSpace() {
            return INameSpace.EMPTY_NAME_SPACE;
        }

        @Override
        public boolean isAssignableFrom(Binding valueCB) {
            return getType().isAssignableFrom(valueCB);
        }

        public Binding getType() {
            return PrimitiveTypeBinding.IntType;
        }

        @Override
        public boolean isStatic() {
            return false;
        }

    }

    private static final class CloneMethodBinding extends AbstractMethodBinding {

        private final ArrayTypeBinding owner;

        public CloneMethodBinding(ArrayTypeBinding owner) {
            this.owner = owner;
        }

        @Override
        public Binding[] getParameters() {
            return new Binding[0];
        }

        @Override
        public Binding getCommonType(Binding b) {
            return owner.getCommonType(b);
        }

        @Override
        public String getName() {
            return owner.getName();
        }

        @Override
        public INameSpace getNameSpace() {
            return owner.getNameSpace();
        }

        @Override
        public boolean isAssignableFrom(Binding valueCB) {
            return owner.isAssignableFrom(valueCB);
        }

        public Binding getType() {
            return owner;
        }

        @Override
        public boolean isStatic() {
            return false;
        }

    }

    public Binding getInnerType() {
        if (arrayCount > 1) {
            return new ArrayTypeBinding(compiler, type, arrayCount - 1);
        }
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ArrayTypeBinding)) {
            return false;
        }
        ArrayTypeBinding other = (ArrayTypeBinding) obj;
        return type.equals(other.type) && arrayCount == other.arrayCount;
    }

}
