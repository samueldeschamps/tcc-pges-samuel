/*
 * Created on 05/03/2007
 */
package japa.compiler.nodes.binding.binary;

import japa.compiler.nodes.binding.ArrayTypeBinding;
import japa.compiler.nodes.binding.Binding;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
public class GenericBinaryClassBinding extends BinaryClassBinding {

    private final Map<String, Binding> types;

    public GenericBinaryClassBinding(BinaryClassBinding inner, List<Binding> types) {
        super(inner.compiler, inner.id, inner.pkg, inner.getType());

        this.types = new HashMap<String, Binding>();
        Class t = getType();
        int i = 0;
        for (TypeVariable p : t.getTypeParameters()) {
            this.types.put(p.getName(), types.get(i++));
        }
    }

    @Override
    public BinaryMethodBinding getMethodBinding(Method method) {
        return new GenericBinaryMethodBinding(this, super.getMethodBinding(method));
    }

    @Override
    protected Binding getBinding(Type t) {
        if (t instanceof GenericArrayType) {
            return new ArrayTypeBinding(compiler, getBinding(((GenericArrayType) t).getGenericComponentType()), 1);
        }
        if (t instanceof TypeVariable) {
            Binding ret = types.get(((TypeVariable) t).getName());
            if (ret != null) {
                return ret;
            }
        }
        if (t instanceof ParameterizedType) {
            ParameterizedType ptype = ((ParameterizedType) t);
            BinaryClassBinding clazz = (BinaryClassBinding) compiler.fileManager.getBinding(((Class) ptype.getRawType()).getName());
            List<Binding> gtypes = new ArrayList<Binding>();
            for (Type inner : ptype.getActualTypeArguments()) {
                gtypes.add(getBinding(inner));
            }
            return new GenericBinaryClassBinding(clazz, gtypes);
        }
        return super.getBinding(t);
    }

}
