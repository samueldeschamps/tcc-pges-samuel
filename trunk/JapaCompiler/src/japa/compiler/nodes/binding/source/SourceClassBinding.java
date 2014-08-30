/*
 * Created on 24/01/2007
 */
package japa.compiler.nodes.binding.source;

import japa.compiler.Compiler;
import japa.compiler.FileManager;
import japa.compiler.RuntimeSemanticException;
import japa.compiler.SemanticException;
import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.AbstractConstructorBinding;
import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.Binding;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Julio Vilmar Gesser
 */
public class SourceClassBinding extends AbstractSourceClassBinding {

	private TypeDeclaration type;

	private Boolean hasConstructor = null;

	public SourceClassBinding(Compiler compiler, String id, String pkg, File javaFile) {
		super(compiler, id, pkg, javaFile);
	}

	public SourceClassBinding(Compiler compiler, String id, String pkg, File javaFile, TypeDeclaration type) {
		this(compiler, id, pkg, javaFile);
		this.type = type;
	}

	@Override
	public final int hashCode() {
		return id.hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SourceClassBinding)) {
			return false;
		}
		SourceClassBinding other = (SourceClassBinding) obj;
		return other.javaFile.equals(javaFile) && other.id.equals(id);
	}

	public final TypeDeclaration getType() {
		if (type == null) {
			CompilationUnit cu;
			try {
				cu = compiler.getCompilationUnit(javaFile);
			} catch (ParseException e) {
				throw new RuntimeSemanticException(e);
			} catch (SemanticException e) {
				throw new RuntimeSemanticException(e);
			}
			for (TypeDeclaration itype : cu.getTypes()) {
				if (itype.getName().equals(id)) {
					type = itype;
					break;
				}
			}
			assert type != null;
		}
		return type;
	}

	@Override
	public final boolean isIntanceOf(AbstractClassBinding valueCB) {
		if (equals(valueCB)) {
			return true;
		}

		TypeDeclaration type_ = getType();
		if (type_ instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration ctype = (ClassOrInterfaceDeclaration) type_;
			if (ctype.getExtends() != null) {
				for (ClassOrInterfaceType ex : ctype.getExtends()) {
					if (valueCB.isAssignableFrom((Binding) ex.getData())) {
						return true;
					}
				}
			}
			if (ctype.getImplements() != null) {
				for (ClassOrInterfaceType imp : ctype.getImplements()) {
					if (valueCB.isAssignableFrom((Binding) imp.getData())) {
						return true;
					}
				}
			}
		}
		return valueCB.getName().equals("java.lang.Object");
	}

	@Override
	public final int getModifiers() {
		return getType().getModifiers();
	}

	@Override
	public final boolean isInterface() {
		return getType() instanceof ClassOrInterfaceDeclaration
				&& ((ClassOrInterfaceDeclaration) getType()).isInterface();
	}

	@Override
	public final boolean isEnum() {
		return getType() instanceof EnumDeclaration;
	}

	@Override
	public Binding getLocalMember(String name) {
		TypeDeclaration ltype = getType();
		return getLocalMember(name, ltype, ltype.getMembers());
	}

	@Override
	public final Binding getMember(String name) {
		Binding ret = getLocalMember(name);
		if (ret != null) {
			return ret;
		}
		for (Iterator<AbstractClassBinding> i = getBasesIterator(); i.hasNext();) {
			AbstractClassBinding next = i.next();
			if (next != null) {
				Binding member = next.getMember(name);
				if (member != null) {
					return member;
				}
			}
		}
		return null;
	}

	@Override
	public Iterator<AbstractClassBinding> getBasesIterator() {
		return new Iterator<AbstractClassBinding>() {

			Iterator<ClassOrInterfaceType> bases;

			Iterator<ClassOrInterfaceType> interfaces;

			Iterator<AbstractClassBinding> cur = null;

			Boolean object = null;

			{
				TypeDeclaration ltype = getType();
				List<ClassOrInterfaceType> extendsList = null;
				if (ltype instanceof ClassOrInterfaceDeclaration) {
					extendsList = ((ClassOrInterfaceDeclaration) ltype).getExtends();
				}
				bases = extendsList == null ? Collections.EMPTY_LIST.iterator() : extendsList.iterator();

				extendsList = null;
				if (ltype instanceof ClassOrInterfaceDeclaration) {
					extendsList = ((ClassOrInterfaceDeclaration) ltype).getImplements();
				} else if (ltype instanceof EnumDeclaration) {
					extendsList = ((EnumDeclaration) ltype).getImplements();
				}
				interfaces = extendsList == null ? null : extendsList.iterator();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

			public AbstractClassBinding next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				if (object != null && object) {
					object = false;
					return (AbstractClassBinding) compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Object");
				}

				if (cur == null) {
					AbstractClassBinding next = (AbstractClassBinding) bases.next().getData();
					cur = next.getBasesIterator();
					return next;
				}
				return cur.next();
			}

			public boolean hasNext() {
				if (cur == null) {
					if (!bases.hasNext()) {
						if (interfaces != null) {
							bases = interfaces;
							interfaces = null;
							return hasNext();
						}
						if (object == null) {
							object = true;
						}
						return object;
					}
					return true;
				}
				if (cur.hasNext()) {
					return true;
				}
				cur = null;
				return hasNext();
			}

		};
	}

	@Override
	public Binding getCommonType(Binding b) {
		b = getInternalType(b);
		if (this.equals(b)) {
			return this;
		}
		if (this.isAssignableFrom(b)) {
			return this;
		}
		if (b.isAssignableFrom(this)) {
			return b;
		}
		if (b instanceof AbstractClassBinding) {
			Iterator<AbstractClassBinding> thisI = getBasesIterator();
			Iterator<AbstractClassBinding> otherI = ((AbstractClassBinding) b).getBasesIterator();
			while (thisI.hasNext()) {
				AbstractClassBinding thisC = thisI.next();
				while (otherI.hasNext()) {
					AbstractClassBinding otherC = otherI.next();
					if (thisC.isAssignableFrom(otherC)) {
						return thisC;
					}
					if (otherC.isAssignableFrom(thisC)) {
						return otherC;
					}
				}
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final void addCompatibleConstructors(List<Binding> args, List<AbstractConstructorBinding> constructors) {
		TypeDeclaration ltype = getType();
		List<BodyDeclaration> members = ltype.getMembers();
		if (members != null) {
			for (BodyDeclaration member : members) {
				if (member instanceof ConstructorDeclaration) {
					ConstructorDeclaration constructor = (ConstructorDeclaration) member;
					if (compareArgs(constructor.getParameters(), args)) {

						SourceConstructorBinding ret = new SourceConstructorBinding(constructor);
						constructors.add(ret);
						if (args == null) {
							return;
						}
					}
				}
			}
		}

		for (Iterator<AbstractClassBinding> i = getBasesIterator(); i.hasNext();) {
			i.next().addCompatibleConstructors(args, constructors);
		}

	}

	@Override
	public final void addCompatibleMethods(String name, List<Binding> args, List<AbstractMethodBinding> methods) {
		TypeDeclaration ltype = getType();
		List<BodyDeclaration> members = ltype.getMembers();
		addCompatibleMethods(name, args, methods, members);
	}

	@Override
	public Binding getGenericType(List<Binding> types) {
		TypeDeclaration ltype = getType();
		if (ltype instanceof ClassOrInterfaceDeclaration) {
			ClassOrInterfaceDeclaration cid = (ClassOrInterfaceDeclaration) ltype;
			if (cid.getTypeParameters() == null || cid.getTypeParameters().size() != types.size()) {
				return null;
			}
			return new GenericSourceClassBinding(this, types);
		}
		return super.getGenericType(types);
	}

	@Override
	public boolean hasConstructors() {
		if (hasConstructor == null) {

			hasConstructor = Boolean.FALSE;

			TypeDeclaration ltype = getType();
			for (BodyDeclaration member : ltype.getMembers()) {
				if (member instanceof ConstructorDeclaration) {
					hasConstructor = Boolean.TRUE;
					break;
				}
			}
			if (!hasConstructor.booleanValue()) {
				if (ltype instanceof ClassOrInterfaceDeclaration
						&& ((ClassOrInterfaceDeclaration) ltype).getExtends() != null) {
					for (ClassOrInterfaceType base : ((ClassOrInterfaceDeclaration) ltype).getExtends()) {
						if (((AbstractClassBinding) base.getData()).hasConstructors()) {
							hasConstructor = Boolean.TRUE;
							break;
						}
					}

				}
			}
		}
		return hasConstructor.booleanValue();
	}

	public AbstractClassBinding getSuper() {
		ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) getType();
		AbstractClassBinding ret;
		if (c.getExtends() != null) {
			ret = (AbstractClassBinding) c.getExtends().get(0).getData();
		} else {
			ret = (AbstractClassBinding) compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Object");
		}
		return ret;
	}
}
