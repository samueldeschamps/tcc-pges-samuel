/*
 * Created on 10/12/2006
 */
package japa.compiler;

import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.AbstractConstructorBinding;
import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.ArrayTypeBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.PrimitiveTypeBinding;
import japa.compiler.nodes.namespace.AmbiguousMethodException;
import japa.compiler.nodes.namespace.INameSpace;
import japa.compiler.nodes.namespace.NamesContext;
import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import japa.parser.ast.type.PrimitiveType.Primitive;
import japa.parser.ast.visitor.GenericVisitor;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Julio Vilmar Gesser
 */
public class SemanticCheckerVisitor implements GenericVisitor<Object, NamesContext> {

    private final Compiler compiler;

    private final File file;

    private final CompilationUnit cu;

    public SemanticCheckerVisitor(Compiler compiler, File file, CompilationUnit cu) {
        this.compiler = compiler;
        this.file = file;
        this.cu = cu;
    }

    public void check() throws SemanticException {
        try {
            visit(cu, null);
        } catch (RuntimeSemanticException e) {
            if (e.getCause() instanceof SemanticException) {
                throw (SemanticException) e.getCause();
            }
            throw new SemanticException(e.getMessage(), e);
        }
    }

    public Object visit(Node n, NamesContext arg) {
        throw new IllegalStateException(n.getClass().getName());
    }

    public Object visit(PackageDeclaration n, NamesContext arg) {
        return null;
    }

    public Object visit(CompilationUnit n, NamesContext arg) {
        if (n.getTypes() != null) {
            for (TypeDeclaration i : n.getTypes()) {
                i.accept(this, null);
            }
        }
        return null;
    }

    public Object visit(NameExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(QualifiedNameExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(ImportDeclaration n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(ClassOrInterfaceDeclaration n, NamesContext arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }

        if (n.getTypeParameters() != null) {
            for (TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }

        if (n.getExtends() != null) {
            for (ClassOrInterfaceType c : n.getExtends()) {
                AbstractClassBinding ret = (AbstractClassBinding) c.getData();

                if (ret.isEnum()) {
                    throw new RuntimeSemanticException("Class or interface type {0} can only extends class or interface types", new Object[] { c.toString() }, c, file);
                }
                if (n.isInterface() && !ret.isInterface()) {
                    throw new RuntimeSemanticException("The interface {0} cannot extends a class", new Object[] { c.toString() }, c, file);
                }
                if (!n.isInterface()) {
                    if (ret.isInterface()) {
                        throw new RuntimeSemanticException("The class {0} cannot extends the interface {1}", new Object[] { n.getName(), c.toString() }, c, file);
                    }
                    if (ModifierSet.isFinal(ret.getModifiers())) {
                        throw new RuntimeSemanticException("The class {0} cannot extends the final class {0}", new Object[] { n.getName(), c.toString() }, c, file);
                    }

                }

            }
        }

        if (n.getImplements() != null) {
            if (n.isInterface()) {
                throw new RuntimeSemanticException("Interface type cannot have implements clause", n.getImplements().get(0), file);
            }

            Set<AbstractClassBinding> intfs = new HashSet<AbstractClassBinding>();

            for (ClassOrInterfaceType c : n.getImplements()) {
                AbstractClassBinding ret = (AbstractClassBinding) c.getData();

                if (!intfs.add(ret)) {
                    throw new RuntimeSemanticException("Duplicate interface: {0}", new Object[] { c.toString() }, c, file);
                }

                if (!ret.isInterface()) {
                    throw new RuntimeSemanticException("Only a interface can be implemented:", new Object[] { c.toString() }, c, file);
                }
            }
        }

        if (n.getMembers() != null) {
            for (BodyDeclaration member : n.getMembers()) {
                member.accept(this, new NamesContext(((Binding) n.getData()).getNameSpace()));
            }
        }

        return null;
    }

    public Object visit(ClassOrInterfaceType n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(TypeParameter n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(EnumDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(EnumConstantDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(AnnotationDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(AnnotationMemberDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(FieldDeclaration n, NamesContext arg) {
        for (VariableDeclarator v : n.getVariables()) {
            v.accept(this, arg);
        }
        return null;
    }

    public Object visit(VariableDeclarator n, NamesContext arg) {
        if (n.getInit() != null) {
            Binding ftype = (Binding) n.getData();
            ftype = Binding.getInternalType(ftype);

            if (n.getInit() != null) {
                Binding itype = (Binding) n.getInit().accept(this, arg);
                itype = Binding.getInternalType(itype);

                if (!ftype.isSpecialAssignableFrom(itype)) {
                    throw new RuntimeSemanticException("Cannot assign a {0} to a {1}", new Object[] { itype.getName(), ftype.getName() }, n, file);
                }
            }
        }
        return n.getData();
    }

    public Object visit(VariableDeclaratorId n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(ConstructorDeclaration n, NamesContext arg) {
        if (n.getParameters() != null) {
            for (Parameter par : n.getParameters()) {
                par.accept(this, arg);
            }
        }

        if (n.getThrows() != null) {
            for (NameExpr name : n.getThrows()) {
                name.accept(this, arg);
            }
        }
        n.getBlock().accept(this, arg);
        return null;
    }

    public Object visit(MethodDeclaration n, NamesContext arg) {
        if (n.getTypeParameters() != null) {
            for (TypeParameter tp : n.getTypeParameters()) {
                tp.accept(this, arg);
            }
        }

        n.getType().accept(this, arg);

        if (n.getParameters() != null) {
            for (Parameter par : n.getParameters()) {
                par.accept(this, arg);
            }
        }

        if (n.getThrows() != null) {
            for (NameExpr ne : n.getThrows()) {
                ne.accept(this, arg);
            }
        }

        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }

        return null;
    }

    public Object visit(Parameter n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(EmptyMemberDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(InitializerDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(PrimitiveType n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(ReferenceType n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(VoidType n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(WildcardType n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(ArrayAccessExpr n, NamesContext arg) {
        Binding type = (Binding) n.getName().accept(this, arg);
        type = Binding.getInternalType(type);
        if (!(type instanceof ArrayTypeBinding)) {
            throw new RuntimeSemanticException("Invalid array access, {0} is not an array, it is a {1}", new Object[] { n.getName(), type.getName() }, n, file);
        }
        n.setData(((ArrayTypeBinding) type).getInnerType());
        return n.getData();
    }

    public Object visit(ArrayCreationExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(ArrayInitializerExpr n, NamesContext arg) {
        Binding ret = null;
        if (n.getValues() != null) {
            for (Expression e : n.getValues()) {
                Binding b = (Binding) e.accept(this, arg);

                b = Binding.getInternalType(b);

                if (ret != null) {
                    ret = ret.getCommonType(b);
                    if (ret == null) {
                        ret = compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Object");
                    }
                } else {
                    ret = b;
                }
            }

            ret = new ArrayTypeBinding(compiler, ret, 1);
        } else {
            ret = new ArrayTypeBinding(compiler, null, 1);
        }
        n.setData(ret);
        return ret;
    }

    public Object visit(AssignExpr n, NamesContext arg) {
        Binding target = (Binding) n.getTarget().accept(this, arg);
        Binding value = (Binding) n.getValue().accept(this, arg);

        target = Binding.getInternalType(target);
        value = Binding.getInternalType(value);

        switch (n.getOperator()) {
            case assign: // =
                if (!target.isSpecialAssignableFrom(value)) {
                    throw new RuntimeSemanticException("Cannot assign a {0} to a {1}", new Object[] { value.getName(), target.getName() }, n, file);
                }
                break;
            case and: // &=
            case or: // |=
            case xor: // ^=
            case lShift: // <<=
            case rSignedShift: // >>=
            case rUnsignedShift: // >>>=
                if (!(target instanceof PrimitiveTypeBinding) || !(value instanceof PrimitiveTypeBinding)) {
                    if (!((PrimitiveTypeBinding) target).isInteger() && ((PrimitiveTypeBinding) value).isInteger()) {
                        throw new RuntimeSemanticException("The operator {0} is undefined for the argument type(s) {1}, {2}", new Object[] { n.getOperator().toString(), target.getName(), value.getName() }, n, file);
                    }
                }
                break;
            case plus: // +=
                if (target instanceof AbstractClassBinding && target.getName().equals(FileManager.JAVA_LANG_STRING)) {
                    // String pode somar com qqr coisa
                    break;
                }
            case minus: // -=
            case star: // *=
            case slash: // /=
            case rem: // %=
                if (!(target instanceof PrimitiveTypeBinding) || !(value instanceof PrimitiveTypeBinding)) {
                    if (!target.isSpecialAssignableFrom(value)) {
                        throw new RuntimeSemanticException("The operator {0} is undefined for the argument type(s) {1}, {2}", new Object[] { n.getOperator().toString(), target.getName(), value.getName() }, n, file);
                    }
                }
                break;
        }
        n.setData(target);
        return value;
    }

    public Object visit(BinaryExpr n, NamesContext arg) {
        Binding leftt = (Binding) n.getLeft().accept(this, arg);
        Binding rightt = (Binding) n.getRight().accept(this, arg);

        leftt = Binding.getInternalType(leftt);
        rightt = Binding.getInternalType(rightt);

        switch (n.getOperator()) {
            case plus:
            case minus:
            case times:
            case divide:
            case remainder:
                n.setData(binaryAritmetic(n, leftt, rightt));
                break;
            case binOr:
            case binAnd:
            case xor:
            case lShift:
            case rSignedShift:
            case rUnsignedShift:
                n.setData(binaryBinary(n, leftt, rightt));
                break;
            case equals:
            case notEquals:
                if (!leftt.isAssignableFrom(rightt) && !rightt.isAssignableFrom(leftt)) {
                    throw new RuntimeSemanticException("Cannot compare a {0} with a {1}", new Object[] { leftt.getName(), rightt.getName() }, n, file);
                }
                n.setData(PrimitiveTypeBinding.BooleanType);
                break;
            case less:
            case greater:
            case lessEquals:
            case greaterEquals:
                boolean valid = false;
                if (leftt instanceof PrimitiveTypeBinding && rightt instanceof PrimitiveTypeBinding) {
                    PrimitiveTypeBinding pl = (PrimitiveTypeBinding) leftt;
                    PrimitiveTypeBinding pr = (PrimitiveTypeBinding) rightt;

                    if (pl.type != PrimitiveType.Primitive.Boolean && pr.type != PrimitiveType.Primitive.Boolean) {
                        valid = true;
                    }
                }
                if (!valid) {
                    throw new RuntimeSemanticException("Cannot compare a {0} with a {1}", new Object[] { leftt.getName(), rightt.getName() }, n, file);
                }
                n.setData(PrimitiveTypeBinding.BooleanType);
                break;
            case or:
            case and:
                valid = false;
                if (leftt instanceof PrimitiveTypeBinding && rightt instanceof PrimitiveTypeBinding) {
                    PrimitiveTypeBinding pl = (PrimitiveTypeBinding) leftt;
                    PrimitiveTypeBinding pr = (PrimitiveTypeBinding) rightt;

                    if (pl.type == PrimitiveType.Primitive.Boolean && pr.type == PrimitiveType.Primitive.Boolean) {
                        valid = true;
                    }
                }
                if (!valid) {
                    throw new RuntimeSemanticException("Cannot compare a {0} with a {1}", new Object[] { leftt.getName(), rightt.getName() }, n, file);
                }
                n.setData(PrimitiveTypeBinding.BooleanType);
                break;
        }
        return n.getData();
    }

    private Binding binaryBinary(BinaryExpr n, Binding leftt, Binding rightt) {

        if (leftt instanceof PrimitiveTypeBinding && rightt instanceof PrimitiveTypeBinding) {
            PrimitiveTypeBinding pl = (PrimitiveTypeBinding) leftt;
            PrimitiveTypeBinding pr = (PrimitiveTypeBinding) rightt;

            if (pl.isInteger() && pr.isInteger()) {

                switch (n.getOperator()) {
                    case binOr:
                        return pl.opOr(pr);
                    case binAnd:
                        return pl.opAnd(pr);
                    case xor:
                        return pl.opXor(pr);
                    case lShift:
                        return pl.opLShift(pr);
                    case rSignedShift:
                        return pl.opSShift(pr);
                    case rUnsignedShift:
                        return pl.opUShift(pr);
                }
            }
            if (pl.type == Primitive.Boolean && pr.type == Primitive.Boolean) {

                switch (n.getOperator()) {
                    case binOr:
                        return pl.opOr(pr);
                    case binAnd:
                        return pl.opAnd(pr);
                    case xor:
                        return pl.opXor(pr);
                }
            }
        }
        throw new RuntimeSemanticException("The operator {0} is undefined for the argument type(s) {1}, {2}", new Object[] { n.getOperator().toString(), leftt.getName(), rightt.getName() }, n, file);
    }

    private Binding binaryAritmetic(BinaryExpr n, Binding leftt, Binding rightt) {
        if (leftt instanceof PrimitiveTypeBinding && rightt instanceof PrimitiveTypeBinding) {
            PrimitiveTypeBinding pl = (PrimitiveTypeBinding) leftt;
            PrimitiveTypeBinding pr = (PrimitiveTypeBinding) rightt;

            if (pl.type != PrimitiveType.Primitive.Boolean && pr.type != PrimitiveType.Primitive.Boolean) {

                switch (n.getOperator()) {
                    case plus:
                        return pl.opPlus(pr);
                    case minus:
                        return pl.opMinus(pr);
                    case times:
                        return pl.opTimes(pr);
                    case divide:
                        return pl.opDivide(pr);
                    case remainder:
                        return pl.opRemainder(pr);
                }
            }
        }
        // string pode somar com qqr coisa
        if (leftt instanceof AbstractClassBinding && leftt.getName().equals(FileManager.JAVA_LANG_STRING)) {
            return leftt;
        }
        if (rightt instanceof AbstractClassBinding && rightt.getName().equals(FileManager.JAVA_LANG_STRING)) {
            return rightt;
        }

        throw new RuntimeSemanticException("The operator {0} is undefined for the argument type(s) {1}, {2}", new Object[] { n.getOperator().toString(), leftt.getName(), rightt.getName() }, n, file);
    }

    public Object visit(CastExpr n, NamesContext arg) {
        Binding type = (Binding) n.getData();
        type = Binding.getInternalType(type);
        Binding value = (Binding) n.getExpr().accept(this, arg);
        value = Binding.getInternalType(value);

        if (!type.isSpecialAssignableFrom(value) && !value.isSpecialAssignableFrom(type)) {
            if (!(type instanceof PrimitiveTypeBinding) || !((PrimitiveTypeBinding) type).canCast(value)) {
                throw new RuntimeSemanticException("Cannot cast from {0} to {1}", new Object[] { value.getName(), type.getName() }, n, file);
            }
        }

        return type;
    }

    public Object visit(ClassExpr n, NamesContext arg) {
        n.getType().accept(this, arg);
        return n.getData();
    }

    public Object visit(ConditionalExpr n, NamesContext arg) {
        Binding condType = (Binding) n.getCondition().accept(this, arg);
        if (!PrimitiveTypeBinding.BooleanType.isAssignableFrom(condType)) {
            throw new RuntimeSemanticException("Expected a boolean, but found a {0}", new Object[] { condType.getName() }, n.getCondition(), file);
        }
        Binding lType = (Binding) n.getThenExpr().accept(this, arg);
        lType = Binding.getInternalType(lType);
        Binding rType = (Binding) n.getElseExpr().accept(this, arg);
        rType = Binding.getInternalType(rType);
        Binding ret = lType.getCommonType(rType);
        if (ret == null) {
            throw new RuntimeSemanticException("Incompatible types {0} and {1}", new Object[] { lType.getName(), rType.getName() }, n.getThenExpr(), file);
        }
        n.setData(ret);
        return ret;
    }

    public Object visit(EnclosedExpr n, NamesContext arg) {
        n.setData(n.getInner().accept(this, arg));
        return n.getData();
    }

    public Object visit(FieldAccessExpr n, NamesContext arg) {
        Binding b = (Binding) n.getScope().accept(this, arg);

        if (n.getTypeArgs() != null) {
            for (Type type : n.getTypeArgs()) {
                type.accept(this, arg);
            }
        }

        Binding ret = b.getNameSpace().resolve(n.getField());
        n.setData(ret);
        return n.getData();
    }

    public Object visit(InstanceOfExpr n, NamesContext arg) {
        Binding instance = (Binding) n.getExpr().accept(this, arg);
        instance = Binding.getInternalType(instance);
        if (!(instance instanceof AbstractClassBinding)) {
            throw new RuntimeSemanticException("Expected an object reference, but found a {0}", new Object[] { instance.getName() }, n.getExpr(), file);
        }

        Binding clazz = (Binding) n.getType().accept(this, arg);
        clazz = Binding.getInternalType(clazz);
        Binding b = clazz;
        if (b instanceof ArrayTypeBinding) {
            b = ((ArrayTypeBinding) clazz).getInnerType();
        }
        if (!(b instanceof AbstractClassBinding)) {
            throw new RuntimeSemanticException("Expected a type reference, but found a {0}", new Object[] { clazz.getName() }, n.getType(), file);
        }

        return n.getData();
    }

    public Object visit(BooleanLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(CharLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(DoubleLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(IntegerLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(LongLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(IntegerLiteralMinValueExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(LongLiteralMinValueExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(NullLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(StringLiteralExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(MethodCallExpr n, NamesContext arg) {
        INameSpace ns;
        if (n.getScope() != null) {
            Binding b = (Binding) n.getScope().accept(this, arg);
            ns = b.getNameSpace();
        } else {
            ns = arg;
        }

        if (n.getTypeArgs() != null) {
            for (Type type : n.getTypeArgs()) {
                type.accept(this, arg);
            }
        }

        if (n.getArgs() != null) {
            for (Expression expr : n.getArgs()) {
                expr.accept(this, arg);
            }
        }

        AbstractMethodBinding method;
        try {
            method = (AbstractMethodBinding) ns.resolve(n.getName(), n.getArgs());
        } catch (AmbiguousMethodException e) {
            throw new RuntimeSemanticException("The method {0}({1}) is ambiguous", new Object[] { n.getName(), getArgsDesc(n.getArgs()) }, n, file);
        }
        if (method == null) {
            throw new RuntimeSemanticException("The method {0}({1}) is undefined", new Object[] { n.getName(), getArgsDesc(n.getArgs()) }, n, file);
        }
        n.setData(method);

        return n.getData();
    }

    private String getArgsDesc(List<Expression> args) {
        StringBuilder buf = new StringBuilder();
        if (args != null) {
            buf.append(Binding.getInternalType(((Binding) args.get(0).getData())).getName());
            for (int i = 1; i < args.size(); i++) {
                buf.append(", ");
                buf.append(Binding.getInternalType(((Binding) args.get(i).getData())).getName());
            }
        }
        return buf.toString();
    }

    public Object visit(ObjectCreationExpr n, NamesContext arg) {
        AbstractClassBinding objType = (AbstractClassBinding) n.getType().getData();

        INameSpace ns;
        if (n.getScope() != null) {
            Binding b = (Binding) n.getScope().accept(this, arg);
            ns = b.getNameSpace();
        } else {
            ns = objType.getNameSpace();
        }

        if (ModifierSet.isAbstract(objType.getModifiers()) && n.getAnonymousClassBody() == null) {
            throw new RuntimeSemanticException("Cannot instantiate the abstact type {0}", new Object[] { n.getType().getName() }, n, file);
        }

        if (n.getTypeArgs() != null) {
            for (Type type : n.getTypeArgs()) {
                type.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression expr : n.getArgs()) {
                expr.accept(this, arg);
            }
        }

        // se for construto default de uma interface, nem procura
        if (!(objType.isInterface() && n.getArgs() == null)) {
            AbstractConstructorBinding constructor;
            try {
                constructor = (AbstractConstructorBinding) ns.resolve("", n.getArgs());
            } catch (AmbiguousMethodException e) {
                throw new RuntimeSemanticException("The constructor {0}({1}) is ambiguous", new Object[] { n.getType().getName(), getArgsDesc(n.getArgs()) }, n, file);
            }
            if (constructor == null) {
                if (n.getArgs() != null || objType.hasConstructors()) {
                    throw new RuntimeSemanticException("The constructor {0}({1}) is undefined", new Object[] { n.getType().getName(), getArgsDesc(n.getArgs()) }, n, file);
                }
            }
        }

        if (n.getAnonymousClassBody() != null) {
            for (BodyDeclaration bd : n.getAnonymousClassBody()) {
                bd.accept(this, arg);
            }
        }

        return objType;
    }

    public Object visit(ThisExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(UnaryExpr n, NamesContext arg) {
        n.setData(n.getExpr().accept(this, arg));
        return n.getData();
    }

    public Object visit(VariableDeclarationExpr n, NamesContext arg) {
        n.getType().accept(this, arg);
        for (VariableDeclarator var : n.getVars()) {
            var.accept(this, arg);
        }
        return n.getData();
    }

    public Object visit(MarkerAnnotationExpr n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(SingleMemberAnnotationExpr n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(NormalAnnotationExpr n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(MemberValuePair n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(ExplicitConstructorInvocationStmt n, NamesContext arg) {
        if (n.getExpr() != null) {
            n.getExpr().accept(this, arg);
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        AbstractConstructorBinding constr = null;
        try {
            constr = (AbstractConstructorBinding) arg.resolve("", n.getArgs());
        } catch (AmbiguousMethodException e) {
            throw new RuntimeSemanticException("The method constructor call {0}({1}) is ambiguous", new Object[] { n.isThis() ? "this" : "super", getArgsDesc(n.getArgs()) }, n, file);
        }
        if (constr == null) {
            throw new RuntimeSemanticException("The constructor {0}({1}) is undefined", new Object[] { n.isThis() ? "this" : "super", getArgsDesc(n.getArgs()) }, n, file);
        }
        return null;
    }

    public Object visit(TypeDeclarationStmt n, NamesContext arg) {
        n.getTypeDeclaration().accept(this, arg);
        return null;
    }

    public Object visit(AssertStmt n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(BlockStmt n, NamesContext arg) {
        if (n.getStmts() != null) {
            for (Statement stmt : n.getStmts()) {
                stmt.accept(this, arg);
            }
        }
        return null;
    }

    public Object visit(LabeledStmt n, NamesContext arg) {
        n.getStmt().accept(this, arg);
        return null;
    }

    public Object visit(EmptyStmt n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(ExpressionStmt n, NamesContext arg) {
        n.getExpression().accept(this, arg);
        return null;
    }

    public Object visit(SwitchStmt n, NamesContext arg) {
        n.getSelector().accept(this, arg);
        Binding b = (Binding) n.getSelector().getData();
        b = Binding.getInternalType(b);
        if (!(b instanceof PrimitiveTypeBinding) && //
                !(b instanceof AbstractClassBinding && ((AbstractClassBinding) b).isEnum())) {
            throw new RuntimeSemanticException("Invalid switch selector argument type: {0}", new Object[] { b.getName() }, n.getSelector(), file);
        }

        if (n.getEntries() != null) {
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        return null;
    }

    public Object visit(SwitchEntryStmt n, NamesContext arg) {
        if (n.getLabel() != null) { // default: não tem label
            n.getLabel().accept(this, arg);
        }
        // TODO: verificar se o label é compativel com o seletor
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
        return null;
    }

    public Object visit(BreakStmt n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(ReturnStmt n, NamesContext arg) {
        if (n.getExpr() != null) {
            n.getExpr().accept(this, arg);
        }
        return null;
    }

    public Object visit(IfStmt n, NamesContext arg) {
        n.getCondition().accept(this, arg);
        Binding b = (Binding) n.getCondition().getData();
        if (!PrimitiveTypeBinding.BooleanType.isAssignableFrom(b)) {
            throw new RuntimeSemanticException("Cannot convert from {0} to boolean", new Object[] { b.getName() }, n.getCondition(), file);
        }
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            n.getElseStmt().accept(this, arg);
        }
        return null;
    }

    public Object visit(WhileStmt n, NamesContext arg) {
        n.getCondition().accept(this, arg);
        Binding b = (Binding) n.getCondition().getData();
        if (!PrimitiveTypeBinding.BooleanType.isAssignableFrom(b)) {
            throw new RuntimeSemanticException("Cannot convert from {0} to boolean", new Object[] { b.getName() }, n.getCondition(), file);
        }

        n.getBody().accept(this, arg);
        return null;
    }

    public Object visit(ContinueStmt n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(DoStmt n, NamesContext arg) {
        n.getBody().accept(this, arg);

        n.getCondition().accept(this, arg);
        Binding b = (Binding) n.getCondition().getData();
        if (!PrimitiveTypeBinding.BooleanType.isAssignableFrom(b)) {
            throw new RuntimeSemanticException("Cannot convert from {0} to boolean", new Object[] { b.getName() }, n.getCondition(), file);
        }
        return null;
    }

    public Object visit(ForeachStmt n, NamesContext arg) {
        // TODO: verificar se o iterable é um Iterable
        // TODO: verificar se o tipo da variável bate com o tipo do iterador
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        return null;
    }

    public Object visit(ForStmt n, NamesContext arg) {
        if (n.getInit() != null) {
            for (Expression expr : n.getInit()) {
                expr.accept(this, arg);
            }
        }
        if (n.getCompare() != null) {
            n.getCompare().accept(this, arg);
            Binding b = (Binding) n.getCompare().getData();
            if (!PrimitiveTypeBinding.BooleanType.isAssignableFrom(b)) {
                throw new RuntimeSemanticException("Cannot convert from {0} to boolean", new Object[] { b.getName() }, n.getCompare(), file);
            }
        }
        if (n.getUpdate() != null) {
            for (Expression expr : n.getUpdate()) {
                expr.accept(this, arg);
            }
        }
        n.getBody().accept(this, arg);
        return null;
    }

    public Object visit(ThrowStmt n, NamesContext arg) {
        Binding b = (Binding) n.getExpr().accept(this, arg);

        AbstractClassBinding throwable = (AbstractClassBinding) compiler.fileManager.getBinding(FileManager.JAVA_LANG + ".Throwable");
        if (!throwable.isAssignableFrom(b)) {
            throw new RuntimeSemanticException("Expected a Throwable but found a {0}", new Object[] { b.getName() }, n.getExpr(), file);
        }

        return null;
    }

    public Object visit(SynchronizedStmt n, NamesContext arg) {
        n.getExpr().accept(this, arg);
        n.getBlock().accept(this, arg);
        return null;
    }

    public Object visit(TryStmt n, NamesContext arg) {
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            n.getFinallyBlock().accept(this, arg);
        }
        return null;
    }

    public Object visit(CatchClause n, NamesContext arg) {
        n.getExcept().accept(this, arg);

        Binding b = (Binding) n.getExcept().getData();
        b = Binding.getInternalType(b);

        AbstractClassBinding throwable = (AbstractClassBinding) compiler.fileManager.getBinding(FileManager.JAVA_LANG + ".Throwable");
        if (!throwable.isAssignableFrom(b)) {
            throw new RuntimeSemanticException("Expected a Throwable but found a {0}", new Object[] { b.getName() }, n.getExcept(), file);
        }

        n.getCatchBlock().accept(this, arg);
        return null;
    }

    public Object visit(EmptyTypeDeclaration n, NamesContext arg) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object visit(SuperExpr n, NamesContext arg) {
        return n.getData();
    }

    public Object visit(JavadocComment n, NamesContext arg) {
        return null;
    }

    public Object visit(LineComment n, NamesContext arg) {
        return null;
    }

    public Object visit(BlockComment n, NamesContext arg) {
        return null;
    }

}
