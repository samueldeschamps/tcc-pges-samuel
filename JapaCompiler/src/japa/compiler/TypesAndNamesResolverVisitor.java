/*
 * Created on 25/02/2007
 */
package japa.compiler;

import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.ArrayTypeBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.GenericTypeBinding;
import japa.compiler.nodes.binding.MemberBinding;
import japa.compiler.nodes.binding.PackageBinding;
import japa.compiler.nodes.binding.PrimitiveTypeBinding;
import japa.compiler.nodes.binding.VariableBinding;
import japa.compiler.nodes.binding.WildcardTypeBinding;
import japa.compiler.nodes.binding.source.AbstractSourceClassBinding;
import japa.compiler.nodes.binding.source.AnnonimousClassBinding;
import japa.compiler.nodes.binding.source.SourceClassBinding;
import japa.compiler.nodes.binding.source.SourceFieldBinding;
import japa.compiler.nodes.binding.source.SourceInnerClassBinding;
import japa.compiler.nodes.namespace.ClassNameSpace;
import japa.compiler.nodes.namespace.CuNameSpace;
import japa.compiler.nodes.namespace.INameSpace;
import japa.compiler.nodes.namespace.NamesContext;
import japa.compiler.nodes.namespace.PackageNameSpace;
import japa.parser.ParseException;
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
import java.util.LinkedList;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class TypesAndNamesResolverVisitor implements GenericVisitor<Object, NamesContext> {

	private final Compiler compiler;

	private final File file;

	private final CompilationUnit cu;

	private final CompilingScope cs;

	public TypesAndNamesResolverVisitor(Compiler compiler, File file, CompilationUnit cu) {
		this.compiler = compiler;
		this.file = file;
		this.cu = cu;
		this.cs = new CompilingScope();
	}

	public void resolve() throws ParseException, SemanticException {
		try {
			NamesContext ns = new NamesContext(new CuNameSpace(compiler, cu, file));
			ns = ns.push(compiler.fileManager);
			visit(cu, ns);
		} catch (RuntimeSemanticException e) {
			if (e.getCause() instanceof ParseException) {
				throw (ParseException) e.getCause();
			}
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
		Binding ret = (Binding) n.getName().accept(this, arg);
		n.setData(ret);

		String expectedPkg = compiler.getExpectedPackage(file);
		if (!(ret instanceof PackageBinding) || (expectedPkg != null && !expectedPkg.equals(ret.getName()))) {
			throw new RuntimeSemanticException("Package {0} does not match expected package {1}", new Object[] {
					n.getName().toString(), expectedPkg }, n, file);
		}

		cs.setPackage(ret.getName());
		return ret;
	}

	public Object visit(CompilationUnit n, NamesContext arg) {
		NamesContext ns = arg;
		// adiciona imports implicitos
		ns = ns.push(new PackageNameSpace(compiler.fileManager, FileManager.JAVA));
		ns = ns.push(new PackageNameSpace(compiler.fileManager, FileManager.JAVA_LANG));

		if (n.getPackage() != null) {
			Binding ret = (Binding) n.getPackage().accept(this, arg);
			// adiciona pacote da classe
			ns = ns.push(ret.getNameSpace());
		}

		if (n.getImports() != null) {
			for (ImportDeclaration i : n.getImports()) {
				INameSpace nameSpace = (INameSpace) i.accept(this, arg);
				i.setData(nameSpace);
				ns = ns.push(nameSpace);
			}
		}

		// emplilha cs para resolver genericos dos extends e implements
		ns = ns.push(cs);

		if (n.getTypes() != null) {
			for (TypeDeclaration i : n.getTypes()) {
				i.accept(this, ns);
			}
		}
		return null;
	}

	public Object visit(NameExpr n, NamesContext arg) {
		Binding binding = arg.resolve(n.getName());
		if (binding != null) {
			if (binding instanceof SourceFieldBinding && !((SourceFieldBinding) binding).isStatic()) {
				if (cs.getClassBinding() != null && !cs.containsSymbol(n.getName())
						&& cs.getClassBinding().getLocalMember(n.getName()) != null) {
					throw new RuntimeSemanticException("Cannot reference the field {0} before it is defined",
							new Object[] { n.getName() }, n, file);
				}
			}
			n.setData(binding);
			return binding;
		}
		throw new RuntimeSemanticException("Symbol not declared: {0}", new Object[] { n.getName() }, n, file);
	}

	public Object visit(QualifiedNameExpr n, NamesContext arg) {
		Binding scope = (Binding) n.getQualifier().accept(this, arg);

		if (scope == null) {
			throw new RuntimeSemanticException("Symbol not declared: {0}", new Object[] { n.getQualifier().getName() },
					n, file);
		}

		INameSpace innerNs = scope.getNameSpace();
		Binding ret = innerNs.resolve(n.getName());
		if (ret == null) {
			throw new RuntimeSemanticException("Symbol not declared: {0}", new Object[] { n.getName() }, n, file);
		}
		n.setData(ret);
		return ret;
	}

	public Object visit(ImportDeclaration n, NamesContext arg) {
		Binding ret = (Binding) n.getName().accept(this, arg);
		n.setData(ret);

		// static classe // invalido
		// static classe.* // ok
		// static classe.m // ok
		// static classe.m.* // invalido
		// static pacote // invalido
		// static pacote.* // ok
		// classe // ok
		// classe.* // ok
		// classe.c // ok
		// classe.m // invalido
		// pacote // invalido
		// pacote.* // ok

		if (n.isStatic()) {
			if (ret instanceof AbstractClassBinding && !n.isAsterisk()) {
				throw new RuntimeSemanticException("Only type members can be imported as static: {0}",
						new Object[] { n.getName() }, n, file);
			}
			if (ret instanceof MemberBinding && n.isAsterisk()) {
				throw new RuntimeSemanticException("The import {0} cannot be resolved", new Object[] { n.getName() },
						n, file);
			}
			if (ret instanceof PackageBinding && !n.isAsterisk()) {
				throw new RuntimeSemanticException("Only type members can be imported as static: {0}",
						new Object[] { n.getName() }, n, file);
			}

			if (ret instanceof AbstractClassBinding) {
				return new ClassNameSpace((AbstractClassBinding) ret, true, true);
			}
			if (ret instanceof MemberBinding) {
				return INameSpace.EMPTY_NAME_SPACE;
			}
			if (ret instanceof PackageBinding) {
				return ret.getNameSpace();
			}
			throw new IllegalStateException(ret.getClass().getName());

		}
		if (ret instanceof MemberBinding) {
			throw new RuntimeSemanticException("Type members must be imported as static: {0}",
					new Object[] { n.getName() }, n, file);
		}
		if (ret instanceof PackageBinding && !n.isAsterisk()) {
			throw new RuntimeSemanticException("Only a type can be imported: {0}", new Object[] { n.getName() }, n,
					file);
		}

		if (ret instanceof AbstractClassBinding) {
			return new ClassNameSpace((AbstractClassBinding) ret, false, n.isAsterisk());
		}
		if (ret instanceof PackageBinding) {
			return ret.getNameSpace();
		}
		throw new IllegalStateException(ret.getClass().getName());
	}

	public Object visit(ClassOrInterfaceDeclaration n, NamesContext arg) {
		if (cs.containsTypeInScope(n.getName())) {
			throw new RuntimeSemanticException("Duplicated type declaration: {0}", new Object[] { n.getName() }, n,
					file);
		}
		cs.addType(n.getName(), n);

		cs.openBlock();

		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}

		if (ModifierSet.isPublic(n.getModifiers()) && cs.getParentClassBinding() == null) {
			// verifica se o tipo publico tem o mesmo nome do arquivo que o
			// declara
			if (!file.getName().substring(0, file.getName().lastIndexOf('.')).equals(n.getName())) {
				throw new RuntimeSemanticException("The public type {0} must be defined in its own file",
						new Object[] { n.getName() }, n, file);
			}
		}

		if (n.getTypeParameters() != null) {
			for (TypeParameter t : n.getTypeParameters()) {
				t.accept(this, arg);
			}
		}

		if (n.getExtends() != null) {
			for (ClassOrInterfaceType c : n.getExtends()) {
				c.accept(this, arg);
			}
		}

		if (n.getImplements() != null) {
			for (ClassOrInterfaceType c : n.getImplements()) {
				c.accept(this, arg);
			}
		}

		AbstractSourceClassBinding thisClassBinding = cs.getParentClassBinding();
		if (thisClassBinding == null) {
			thisClassBinding = (AbstractSourceClassBinding) compiler.fileManager.getBindingFromCache(cs.getPackage(),
					n.getName());
			if (thisClassBinding == null) {
				throw new IllegalStateException("Class to compile was not parserd: " + n.getName());
			}
		} else {
			thisClassBinding = new SourceInnerClassBinding(thisClassBinding, n.getName(), n);
		}
		cs.setClassBinding(thisClassBinding);
		n.setData(thisClassBinding);
		NamesContext ns = arg;
		ns = ns.push(thisClassBinding.getNameSpace());

		// resolverdor de variaveis e tipos locais, já esta empilhado, mas
		// precisa ficar no topo da pilha
		ns = ns.push(cs);

		if (n.getMembers() != null) {
			for (BodyDeclaration member : n.getMembers()) {
				member.accept(this, ns);
			}
		}

		cs.closeBlock();

		return null;
	}

	public Object visit(ClassOrInterfaceType n, NamesContext arg) {
		INameSpace innerNs;
		if (n.getScope() != null) {
			Binding scope = (Binding) n.getScope().accept(this, arg);

			if (scope == null) {
				throw new RuntimeSemanticException("Symbol not declared: {0}", new Object[] { n.getScope().getName() },
						n, file);
			}
			innerNs = scope.getNameSpace();
		} else {
			innerNs = arg;
		}

		Binding ret = innerNs.resolveClass(n.getName());
		if (ret == null) {
			throw new RuntimeSemanticException("Symbol not declared: {0}", new Object[] { n.getName() }, n, file);
		}

		if (n.getTypeArgs() != null) {
			List<Binding> types = new LinkedList<Binding>();
			for (Type type : n.getTypeArgs()) {
				type.accept(this, arg);
				types.add((Binding) type.getData());
			}
			ret = ret.getGenericType(types);
			if (ret == null) {
				throw new RuntimeSemanticException("Invalid generig declaration", n, file);
			}
		}

		n.setData(ret);
		return ret;
	}

	public Object visit(TypeParameter n, NamesContext arg) {
		if (n.getTypeBound() != null) {
			for (ClassOrInterfaceType c : n.getTypeBound()) {
				c.accept(this, arg);
			}
		}

		GenericTypeBinding b = new GenericTypeBinding(compiler, n.getName());
		cs.addGenericType(n.getName(), b);

		n.setData(b);
		return null;
	}

	public Object visit(EnumDeclaration n, NamesContext arg) {
		if (cs.containsTypeInScope(n.getName())) {
			throw new RuntimeSemanticException("Duplicated type declaration: {0}", new Object[] { n.getName() }, n,
					file);
		}
		cs.addType(n.getName(), n);

		cs.openBlock();

		if (n.getAnnotations() != null) {
			for (AnnotationExpr a : n.getAnnotations()) {
				a.accept(this, arg);
			}
		}

		if (ModifierSet.isPublic(n.getModifiers()) && cs.getParentClassBinding() == null) {
			// verifica se o tipo publico tem o mesmo nome do arquivo que o
			// declara
			if (!file.getName().substring(0, file.getName().lastIndexOf('.')).equals(n.getName())) {
				throw new RuntimeSemanticException("The public type {0} must be defined in its own file",
						new Object[] { n.getName() }, n, file);
			}
		}

		if (n.getImplements() != null) {
			for (ClassOrInterfaceType c : n.getImplements()) {
				c.accept(this, arg);
			}
		}

		AbstractSourceClassBinding thisClassBinding = cs.getParentClassBinding();
		if (thisClassBinding == null) {
			thisClassBinding = (AbstractSourceClassBinding) compiler.fileManager.getBindingFromCache(cs.getPackage(),
					n.getName());
			if (thisClassBinding == null) {
				throw new IllegalStateException("Class to compile was not parserd: " + n.getName());
			}
		} else {
			thisClassBinding = new SourceInnerClassBinding(thisClassBinding, n.getName(), n);
		}
		cs.setClassBinding(thisClassBinding);
		n.setData(thisClassBinding);
		NamesContext ns = arg;
		ns = ns.push(thisClassBinding.getNameSpace());

		// resolverdor de variaveis e tipos locais, já esta empilhado, mas
		// precisa ficar no topo da pilha
		ns = ns.push(cs);

		if (n.getMembers() != null) {
			for (BodyDeclaration member : n.getMembers()) {
				member.accept(this, ns);
			}
		}

		cs.closeBlock();
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
		Binding ftype = (Binding) n.getType().accept(this, arg);
		int i = 0;
		for (VariableDeclarator v : n.getVariables()) {
			v.setData(ftype);
			v.accept(this, arg);
			v.setData(new SourceFieldBinding(n, i++, (Binding) v.getData()));
		}
		return null;
	}

	public Object visit(VariableDeclarator n, NamesContext arg) {
		if (cs.containsSymbolInScope(n.getId().getName())) {
			throw new RuntimeSemanticException("Duplicated variable declaration: {0}", new Object[] { n.getId()
					.getName() }, n, file);
		}
		cs.addSymbol(n.getId().getName(), n);

		if (n.getId().getArrayCount() > 0) {
			Binding b = (Binding) n.getData();
			b = Binding.getInternalType(b);
			n.setData(new ArrayTypeBinding(compiler, b, n.getId().getArrayCount()));
		}

		if (n.getInit() != null) {
			n.getInit().accept(this, arg);
		}

		return null;
	}

	public Object visit(VariableDeclaratorId n, NamesContext arg) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(ConstructorDeclaration n, NamesContext arg) {
		cs.openBlock();
		if (n.getTypeParameters() != null) {
			for (TypeParameter tp : n.getTypeParameters()) {
				tp.accept(this, arg);
			}
		}

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

		cs.closeBlock();
		return null;
	}

	public Object visit(MethodDeclaration n, NamesContext arg) {
		cs.openBlock();
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

		cs.closeBlock();
		return null;
	}

	public Object visit(Parameter n, NamesContext arg) {
		Binding type = (Binding) n.getType().accept(this, arg);

		if (cs.containsSymbolInScope(n.getId().getName())) {
			throw new RuntimeSemanticException("Duplacated parameter declaration: {0}", new Object[] { n.getId()
					.getName() }, n, file);
		}
		cs.addSymbol(n.getId().getName(), n);

		int arrayCount = n.getId().getArrayCount();
		if (n.isVarArgs()) {
			arrayCount++;
		}
		if (arrayCount > 0) {
			type = new ArrayTypeBinding(compiler, type, arrayCount);
		}

		n.setData(new VariableBinding(n.getId().getName(), type));

		return null;
	}

	public Object visit(EmptyMemberDeclaration n, NamesContext arg) {
		return null;
	}

	public Object visit(InitializerDeclaration n, NamesContext arg) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(PrimitiveType n, NamesContext arg) {
		Binding ret;
		switch (n.getType()) {
			case Boolean:
				ret = PrimitiveTypeBinding.BooleanType;
				break;
			case Byte:
				ret = PrimitiveTypeBinding.ByteType;
				break;
			case Char:
				ret = PrimitiveTypeBinding.CharType;
				break;
			case Double:
				ret = PrimitiveTypeBinding.DoubleType;
				break;
			case Float:
				ret = PrimitiveTypeBinding.FloatType;
				break;
			case Int:
				ret = PrimitiveTypeBinding.IntType;
				break;
			case Long:
				ret = PrimitiveTypeBinding.LongType;
				break;
			case Short:
				ret = PrimitiveTypeBinding.ShortType;
				break;
			default:
				throw new IllegalStateException(n.getType().toString());
		}
		n.setData(ret);
		return ret;
	}

	public Object visit(ReferenceType n, NamesContext arg) {
		Binding ret = (Binding) n.getType().accept(this, arg);
		if (n.getArrayCount() > 0) {
			ret = new ArrayTypeBinding(compiler, ret, n.getArrayCount());
		}
		n.setData(ret);
		return ret;
	}

	public Object visit(VoidType n, NamesContext arg) {
		n.setData(PrimitiveTypeBinding.NullType);
		return null;
	}

	public Object visit(WildcardType n, NamesContext arg) {
		n.setData(new WildcardTypeBinding());
		return null;
	}

	public Object visit(ArrayAccessExpr n, NamesContext arg) {
		n.getName().accept(this, arg);
		n.setData(n.getName().getData());
		return null;
	}

	public Object visit(ArrayCreationExpr n, NamesContext arg) {
		int arrayCount = n.getArrayCount();
		Binding type = (Binding) n.getType().accept(this, arg);

		if (n.getInitializer() != null) {
			n.getInitializer().accept(this, arg);
		} else {
			for (Expression e : n.getDimensions()) {
				e.accept(this, arg);
			}
			arrayCount += n.getDimensions().size();
		}
		ArrayTypeBinding ret = new ArrayTypeBinding(compiler, type, arrayCount);
		n.setData(ret);
		return null;
	}

	public Object visit(ArrayInitializerExpr n, NamesContext arg) {
		if (n.getValues() != null) {
			for (Expression e : n.getValues()) {
				e.accept(this, arg);
			}
		}
		return null;
	}

	public Object visit(AssignExpr n, NamesContext arg) {
		n.getTarget().accept(this, arg);
		n.getValue().accept(this, arg);
		return null;
	}

	public Object visit(BinaryExpr n, NamesContext arg) {
		n.getLeft().accept(this, arg);
		n.getRight().accept(this, arg);
		return null;
	}

	public Object visit(CastExpr n, NamesContext arg) {
		Binding type = (Binding) n.getType().accept(this, arg);
		n.setData(type);
		n.getExpr().accept(this, arg);
		return null;
	}

	public Object visit(ClassExpr n, NamesContext arg) {
		n.getType().accept(this, arg);
		n.setData(compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Class"));
		return null;
	}

	public Object visit(ConditionalExpr n, NamesContext arg) {
		n.getCondition().accept(this, arg);
		n.getThenExpr().accept(this, arg);
		n.getElseExpr().accept(this, arg);
		return null;
	}

	public Object visit(EnclosedExpr n, NamesContext arg) {
		n.getInner().accept(this, arg);
		return null;
	}

	public Object visit(FieldAccessExpr n, NamesContext arg) {
		Binding b = (Binding) n.getScope().accept(this, arg);
		b = Binding.getInternalType(b);

		if (n.getTypeArgs() != null) {
			for (Type type : n.getTypeArgs()) {
				type.accept(this, arg);
			}
		}

		n.setData(b);
		return null;
	}

	public Object visit(InstanceOfExpr n, NamesContext arg) {
		n.getExpr().accept(this, arg);
		n.getType().accept(this, arg);
		n.setData(PrimitiveTypeBinding.BooleanType);
		return null;
	}

	public Object visit(BooleanLiteralExpr n, NamesContext arg) {
		n.setData(new PrimitiveTypeBinding(n.getValue(), Primitive.Boolean));
		return null;
	}

	public Object visit(DoubleLiteralExpr n, NamesContext arg) {
		try {
			if (n.getValue().endsWith("f") || n.getValue().endsWith("F")) {
				float value = Float.parseFloat(n.getValue());
				n.setData(new PrimitiveTypeBinding(new Float(value), Primitive.Float));
			} else {
				double value = Double.parseDouble(n.getValue());
				n.setData(new PrimitiveTypeBinding(new Double(value), Primitive.Double));
			}
		} catch (NumberFormatException e) {
			throw new RuntimeSemanticException("Invalid floating literal format", n, file);
		}
		return null;
	}

	public Object visit(CharLiteralExpr n, NamesContext arg) {
		char ret;
		if (n.getValue().charAt(0) != '\\') {
			// just a letter
			ret = n.getValue().charAt(0);
		} else if (n.getValue().charAt(1) != 'u') {
			switch (n.getValue().charAt(1)) {
				case 'b':
					ret = '\b';
					break;
				case 't':
					ret = '\t';
					break;
				case 'n':
					ret = '\n';
					break;
				case 'f':
					ret = '\f';
					break;
				case 'r':
					ret = '\r';
					break;
				case '\"':
					ret = '\"';
					break;
				case '\'':
					ret = '\'';
					break;
				case '\\':
					ret = '\\';
					break;
				default:
					// octal
					ret = (char) Integer.parseInt(n.getValue().substring(1, n.getValue().length()), 8);
			}
		} else {
			// unicode
			ret = (char) Integer.parseInt(n.getValue().substring(2, 6), 16);
		}

		n.setData(new PrimitiveTypeBinding(Character.valueOf(ret), Primitive.Char));
		return null;
	}

	public Object visit(IntegerLiteralExpr n, NamesContext arg) {
		final int length = n.getValue().length();
		long computedValue = 0L;

		if (n.getValue().charAt(0) == '0') { // octal or hex
			if (length > 1) {
				final int shift;
				final int radix;
				int j;
				if ((n.getValue().charAt(1) == 'x') || (n.getValue().charAt(1) == 'X')) {
					radix = 16;
					shift = 4;
					j = 2;
				} else {
					radix = 8;
					shift = 3;
					j = 1;
				}
				while (n.getValue().charAt(j) == '0') {
					j++; // jump over redondant zero
					if (j == length) {
						break;
					}
				}
				if (j != length) { // watch for 000000000000000000
					while (j < length) {
						int digitValue = Character.digit(n.getValue().charAt(j++), radix);
						computedValue = (computedValue << shift) | digitValue;
						if (computedValue > 0xFFFFFFFFL) {
							throw new RuntimeSemanticException("Integer literal is out of range: {0}",
									new Object[] { n.getValue() }, n, file);
						}
					}
				}
			}
		} else { // decimal, radix = 10
			for (int i = 0; i < length; i++) {
				int digitValue = Character.digit(n.getValue().charAt(i), 10);
				computedValue = 10 * computedValue + digitValue;
				if (computedValue > Integer.MAX_VALUE) {
					throw new RuntimeSemanticException("Integer literal is out of range: {0}",
							new Object[] { n.getValue() }, n, file);
				}
			}
		}

		n.setData(new PrimitiveTypeBinding(Integer.valueOf((int) computedValue), Primitive.Int));
		return null;
	}

	public Object visit(LongLiteralExpr n, NamesContext arg) {
		int length = n.getValue().length() - 1; // minus one because the last
												// char is 'l' or 'L'
		long computedValue = 0L;

		if (n.getValue().charAt(0) == '0') {
			if (length > 1) {
				final int shift, radix;
				int j;
				if ((n.getValue().charAt(1) == 'x') || (n.getValue().charAt(1) == 'X')) {
					shift = 4;
					j = 2;
					radix = 16;
				} else {
					shift = 3;
					j = 1;
					radix = 8;
				}
				int nbDigit = 0;
				while (n.getValue().charAt(j) == '0') {
					j++; // jump over redondant zero
					if (j == length) {
						break;
					}
				}
				if (j != length) { // watch for 0000000000000L

					int digitValue = Character.digit(n.getValue().charAt(j++), radix);
					if (digitValue >= 8) {
						nbDigit = 4;
					} else if (digitValue >= 4) {
						nbDigit = 3;
					} else if (digitValue >= 2) {
						nbDigit = 2;
					} else {
						nbDigit = 1; // digitValue is not 0
					}
					computedValue = digitValue;
					while (j < length) {
						digitValue = Character.digit(n.getValue().charAt(j++), radix);
						if ((nbDigit += shift) > 64) {
							throw new RuntimeSemanticException("Long literal is out of range: {0}",
									new Object[] { n.getValue() }, n, file);
						}
						computedValue = (computedValue << shift) | digitValue;
					}
				}
			}
		} else {
			// -----------case radix=10-----------------
			long previous = 0;
			computedValue = 0;
			final long limit = Long.MAX_VALUE / 10; // needed to check prior to
													// the multiplication
			for (int i = 0; i < length; i++) {
				int digitValue = Character.digit(n.getValue().charAt(i), 10);
				previous = computedValue;
				if (computedValue > limit) {
					throw new RuntimeSemanticException("Long literal is out of range: {0}",
							new Object[] { n.getValue() }, n, file);
				}
				computedValue *= 10;
				if ((computedValue + digitValue) > Long.MAX_VALUE) {
					throw new RuntimeSemanticException("Long literal is out of range: {0}",
							new Object[] { n.getValue() }, n, file);
				}
				computedValue += digitValue;
				if (previous > computedValue) {
					throw new RuntimeSemanticException("Long literal is out of range: {0}",
							new Object[] { n.getValue() }, n, file);
				}
			}
		}
		n.setData(new PrimitiveTypeBinding(Long.valueOf(computedValue), Primitive.Long));
		return null;
	}

	public Object visit(IntegerLiteralMinValueExpr n, NamesContext arg) {
		n.setData(new PrimitiveTypeBinding(Integer.valueOf(Integer.MIN_VALUE), Primitive.Int));
		return null;
	}

	public Object visit(LongLiteralMinValueExpr n, NamesContext arg) {
		n.setData(new PrimitiveTypeBinding(Long.valueOf(Long.MIN_VALUE), Primitive.Long));
		return null;
	}

	public Object visit(NullLiteralExpr n, NamesContext arg) {
		n.setData(PrimitiveTypeBinding.NullType);
		return null;
	}

	public Object visit(StringLiteralExpr n, NamesContext arg) {
		n.setData(compiler.fileManager.getBinding(FileManager.JAVA_LANG_STRING));
		return null;
	}

	public Object visit(MethodCallExpr n, NamesContext arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
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
		return null;
	}

	public Object visit(ObjectCreationExpr n, NamesContext arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
		}
		Binding b = (Binding) n.getType().accept(this, arg);
		n.setData(b);

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

		if (n.getAnonymousClassBody() != null) {
			cs.openBlock();
			AnnonimousClassBinding acb = new AnnonimousClassBinding(compiler, (AbstractClassBinding) n.getData(),
					n.getAnonymousClassBody(), file);
			n.setData(acb);
			cs.setClassBinding(acb);
			for (BodyDeclaration bd : n.getAnonymousClassBody()) {
				bd.accept(this, arg);
			}
			cs.closeBlock();
		}

		return null;
	}

	public Object visit(ThisExpr n, NamesContext arg) {
		if (n.getClassExpr() != null) {
			n.setData(n.getClassExpr().accept(this, arg));
		} else {
			n.setData(cs.getParentClassBinding());
		}
		return null;
	}

	public Object visit(UnaryExpr n, NamesContext arg) {
		n.getExpr().accept(this, arg);
		return null;
	}

	public Object visit(VariableDeclarationExpr n, NamesContext arg) {
		n.setData(n.getType().accept(this, arg));
		for (VariableDeclarator v : n.getVars()) {
			v.setData(n.getData());
			v.accept(this, arg);
			v.setData(new VariableBinding(v.getId().getName(), (Binding) v.getData()));
		}
		return null;
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
		if (cs.containsSymbolInScope(n.getLabel())) {
			throw new RuntimeSemanticException("Duplacated variable declaration: {0}", new Object[] { n.getLabel() },
					n, file);
		}
		cs.addSymbol(n.getLabel(), n);

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
		cs.openBlock();
		switchSelector = n.getSelector();
		n.getSelector().accept(this, arg);
		if (n.getEntries() != null) {
			for (SwitchEntryStmt e : n.getEntries()) {
				e.accept(this, arg);
			}
		}
		cs.closeBlock();
		return null;
	}

	private Expression switchSelector;

	private EnumConstantDeclaration findConstantIfSelectorIsEnum(String name) {
		if (switchSelector.getData() instanceof VariableBinding) {
			VariableBinding var = ((VariableBinding) switchSelector.getData());
			if (var.getType() instanceof SourceClassBinding) {
				SourceClassBinding clazz = ((SourceClassBinding) var.getType());
				TypeDeclaration innerType = clazz.getType();
				if (innerType instanceof EnumDeclaration) {
					EnumDeclaration enumDecl = ((EnumDeclaration) innerType);
					EnumConstantDeclaration entry = enumDecl.findEntry(name);
					if (entry != null) {
						entry.setData(clazz);
						return entry;
					}
				}
			}
		}
		return null;
	}

	public Object visit(SwitchEntryStmt n, NamesContext arg) {
		if (n.getLabel() != null) { // default: não tem label
			cs.openBlock();
			Expression label = n.getLabel();
			if (label instanceof NameExpr) {
				String name = ((NameExpr) label).getName();
				EnumConstantDeclaration constant = findConstantIfSelectorIsEnum(name);
				if (constant != null) {
					cs.addSymbol(name, constant);
				}
			}
			label.accept(this, arg);
			cs.closeBlock();
		}
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
		cs.openBlock();
		n.getThenStmt().accept(this, arg);
		cs.closeBlock();
		if (n.getElseStmt() != null) {
			cs.openBlock();
			n.getElseStmt().accept(this, arg);
			cs.closeBlock();
		}
		return null;
	}

	public Object visit(WhileStmt n, NamesContext arg) {
		n.getCondition().accept(this, arg);
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
		return null;
	}

	public Object visit(ForeachStmt n, NamesContext arg) {
		cs.openBlock();
		n.getVariable().accept(this, arg);
		n.getIterable().accept(this, arg);
		n.getBody().accept(this, arg);
		cs.closeBlock();
		return null;
	}

	public Object visit(ForStmt n, NamesContext arg) {
		cs.openBlock();
		if (n.getInit() != null) {
			for (Expression expr : n.getInit()) {
				expr.accept(this, arg);
			}
		}
		if (n.getCompare() != null) {
			n.getCompare().accept(this, arg);
		}
		if (n.getUpdate() != null) {
			for (Expression expr : n.getUpdate()) {
				expr.accept(this, arg);
			}
		}
		n.getBody().accept(this, arg);
		cs.closeBlock();
		return null;
	}

	public Object visit(ThrowStmt n, NamesContext arg) {
		n.getExpr().accept(this, arg);
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
		n.getCatchBlock().accept(this, arg);
		return null;
	}

	public Object visit(EmptyTypeDeclaration n, NamesContext arg) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visit(SuperExpr n, NamesContext arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
		}
		AbstractSourceClassBinding b = cs.getParentClassBinding();
		if (b.isEnum() || b.isInterface() || b instanceof AnnonimousClassBinding) {
			throw new RuntimeSemanticException("Cannot call super", n, file);
		}
		AbstractClassBinding ret = ((SourceClassBinding) b).getSuper();
		n.setData(ret);
		return null;
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
