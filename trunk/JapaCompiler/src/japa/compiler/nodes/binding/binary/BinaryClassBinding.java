/*
 * Created on 24/01/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.Compiler;
import japa.compiler.FileManager;
import japa.compiler.nodes.binding.AbstractClassBinding;
import japa.compiler.nodes.binding.AbstractConstructorBinding;
import japa.compiler.nodes.binding.AbstractMethodBinding;
import japa.compiler.nodes.binding.ArrayTypeBinding;
import japa.compiler.nodes.binding.Binding;
import japa.compiler.nodes.binding.PrimitiveTypeBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public class BinaryClassBinding extends AbstractClassBinding {

    private Class type;

    protected BinaryClassBinding(Compiler compiler, String id, String pkg) {
        super(compiler, id, pkg);
    }

    protected BinaryClassBinding(Compiler compiler, String id, String pkg, Class type) {
        this(compiler, id, pkg);
        this.type = type;
    }

    public static Binding classToType(Compiler compiler, Class type) {
        int arrCount = 0;
        while (type.isArray()) {
            arrCount++;
            type = type.getComponentType();
        }
        Binding ret;
        if (type.isPrimitive()) {
            if (type == char.class) {
                ret = PrimitiveTypeBinding.CharType;
            } else if (type == boolean.class) {
                ret = PrimitiveTypeBinding.BooleanType;
            } else if (type == byte.class) {
                ret = PrimitiveTypeBinding.ByteType;
            } else if (type == short.class) {
                ret = PrimitiveTypeBinding.ShortType;
            } else if (type == int.class) {
                ret = PrimitiveTypeBinding.IntType;
            } else if (type == long.class) {
                ret = PrimitiveTypeBinding.LongType;
            } else if (type == float.class) {
                ret = PrimitiveTypeBinding.FloatType;
            } else if (type == double.class) {
                ret = PrimitiveTypeBinding.DoubleType;
            } else if (type == void.class) {
                ret = PrimitiveTypeBinding.NullType;
            } else {
                throw new IllegalStateException(type.toString());
            }
        } else {
            if (type.isMemberClass()) {
                ret = new BinaryClassBinding(compiler, type.getSimpleName(), type.getDeclaringClass().getCanonicalName(), type);
            } else {
                ret = compiler.fileManager.getBinding(type.getPackage() == null ? "" : type.getPackage().getName(), type.getSimpleName());
            }
        }
        if (arrCount > 0) {
            ret = new ArrayTypeBinding(compiler, ret, arrCount);
        }
        return ret;
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
        if (!(obj instanceof BinaryClassBinding)) {
            return false;
        }
        BinaryClassBinding other = (BinaryClassBinding) obj;
        return other.getName().equals(getName());
    }

    protected final Class< ? > getType() {
        if (type == null) {
            type = innerGetType();
        }
        return type;
    }

    protected Class innerGetType() {
        throw new IllegalStateException();
    }

    @Override
    public boolean isIntanceOf(AbstractClassBinding valueCB) {
        if (valueCB instanceof BinaryClassBinding) {
            return ((BinaryClassBinding) valueCB).getType().isAssignableFrom(getType());
        }
        return isInstanceOf(getType(), valueCB.getName());
    }

    private boolean isInstanceOf(Class type_, String name) {
        if (type_.getSuperclass() == null) {
            return false;
        }
        if (type_.getSuperclass().getName().equals(name)) {
            return true;
        }
        if (isInstanceOf(type_.getSuperclass(), name)) {
            return true;
        }
        Class[] interfaces = type_.getInterfaces();
        for (Class element : interfaces) {
            if (element.getName().equals(name)) {
                return true;
            }
            if (element.getSuperclass() != null && isInstanceOf(element.getSuperclass(), name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean isEnum() {
        return getType().isEnum();
    }

    @Override
    public final boolean isInterface() {
        return getType().isInterface();
    }

    @Override
    public final int getModifiers() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Binding getMember(String name) {
        Binding ret = loadedMembers.get(name);
        if (ret != null) {
            return ret;
        }

        Class ltype = getType();

        Class[] classes = ltype.getClasses();
        for (Class element : classes) {
            if (element.getName().equals(getName() + "$" + name)) {
                ret = new BinaryClassBinding(compiler, id + "." + name, pkg, element);
                loadedMembers.put(name, ret);
                return ret;
            }
        }

        try {
            Field field = ltype.getDeclaredField(name);
            ret = new BinaryFieldBinding(compiler, field);
            loadedMembers.put(name, ret);
            return ret;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
        }

        return null;
    }

    @Override
    public Binding getCommonType(Binding b) {
        if (b == PrimitiveTypeBinding.NullType) {
            return this;
        }

        if (this.equals(b)) {
            return this;
        }

        return null;
    }

    @Override
    public final void addCompatibleConstructors(List<Binding> args, List<AbstractConstructorBinding> constructors) {

        Class ltype = getType();

        for (Constructor constructor : ltype.getConstructors()) {
            Class[] argTypes = constructor.getParameterTypes();
            if (argTypes.length != (args == null ? 0 : args.size())) {
                continue;
            }

            for (int i = 0; i < argTypes.length; i++) {
                if (!argTypes[i].getName().equals(args.get(i).getName())) {
                    continue;
                }
            }

            AbstractConstructorBinding ret = new BinaryConstructorBinding(compiler, constructor);
            constructors.add(ret);
            if (args == null) {
                return;
            }
        }
    }

    @Override
    public void addCompatibleMethods(String name, List<Binding> args, List<AbstractMethodBinding> methods) {
        Class ltype = getType();

        methodLoop: for (Method method : ltype.getDeclaredMethods()) {
            if (!name.equals(method.getName())) {
                continue;
            }
            Type[] argTypes = method.getGenericParameterTypes();
            if (method.isVarArgs()) {
                int size = args != null ? args.size() : 0;
                if (size < argTypes.length) {
                    continue methodLoop;
                }
                for (int i = 0; i < argTypes.length - 1; i++) {
                    if (!getBinding(argTypes[i]).isAssignableFrom(args.get(i))) {
                        continue methodLoop;
                    }
                }

                Class last = (Class) argTypes[argTypes.length - 1];
                last = last.getComponentType();
                Binding lastB = getBinding(last);
                for (int i = argTypes.length; i < size; i++) {
                    if (!lastB.isAssignableFrom(args.get(i))) {
                        continue methodLoop;
                    }
                }
            } else {
                if (argTypes.length != (args == null ? 0 : args.size())) {
                    continue methodLoop;
                }

                for (int i = 0; i < argTypes.length; i++) {
                    if (!getBinding(argTypes[i]).isAssignableFrom(args.get(i))) {
                        continue methodLoop;
                    }
                }
            }

            AbstractMethodBinding methodBinding = getMethodBinding(method);
            methods.add(methodBinding);
            if (args == null) {
                return;
            }
        }

        if (ltype.getSuperclass() != null) {
            ((BinaryClassBinding) classToType(compiler, ltype.getSuperclass())).addCompatibleMethods(name, args, methods);
        }
        Class[] interfaces = ltype.getInterfaces();
        for (Class element : interfaces) {
            ((BinaryClassBinding) classToType(compiler, element)).addCompatibleMethods(name, args, methods);
        }
    }

    protected BinaryMethodBinding getMethodBinding(Method method) {
        return new BinaryMethodBinding(compiler, method);
    }

    protected Binding getBinding(Type t) {
        if (t instanceof Class) {
            return classToType(compiler, (Class) t);
        }
        return compiler.fileManager.getBinding(FileManager.JAVA_LANG, "Object");
    }

    @Override
    public Iterator<AbstractClassBinding> getBasesIterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    @Override
    public Binding getGenericType(List<Binding> types) {
        return new GenericBinaryClassBinding(this, types);
    }

    @Override
    public boolean hasConstructors() {
        return getType().getConstructors().length > 0;
    }
}
