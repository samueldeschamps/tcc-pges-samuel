/*
 * Created on 30/01/2007
 */
package japa.compiler.nodes.binding;

import japa.compiler.FileManager;
import japa.compiler.nodes.namespace.INameSpace;
import japa.parser.ast.type.PrimitiveType.Primitive;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
public final class PrimitiveTypeBinding extends Binding {

    public static final PrimitiveTypeBinding NullType = new PrimitiveTypeBinding(null, null);

    public static final PrimitiveTypeBinding ByteType = new PrimitiveTypeBinding(null, Primitive.Byte);

    public static final PrimitiveTypeBinding ShortType = new PrimitiveTypeBinding(null, Primitive.Short);

    public static final PrimitiveTypeBinding IntType = new PrimitiveTypeBinding(null, Primitive.Int);

    public static final PrimitiveTypeBinding LongType = new PrimitiveTypeBinding(null, Primitive.Long);

    public static final PrimitiveTypeBinding FloatType = new PrimitiveTypeBinding(null, Primitive.Float);

    public static final PrimitiveTypeBinding DoubleType = new PrimitiveTypeBinding(null, Primitive.Double);

    public static final PrimitiveTypeBinding CharType = new PrimitiveTypeBinding(null, Primitive.Char);

    public static final PrimitiveTypeBinding BooleanType = new PrimitiveTypeBinding(null, Primitive.Boolean);

    private static Map<String, PrimitiveTypeBinding> unboxingMap;

    public final Primitive type;

    public final Object value;

    public PrimitiveTypeBinding(Object value, Primitive type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public INameSpace getNameSpace() {
        return INameSpace.EMPTY_NAME_SPACE;
    }

    @Override
    public String getName() {
        if (type == null) {
            return "void";
        }
        return type.toString().toLowerCase();
    }

    @Override
    public boolean isAssignableFrom(Binding valueCB) {
        valueCB = getInternalType(valueCB);

        if (valueCB instanceof AbstractClassBinding) {
            PrimitiveTypeBinding unboxing = getUnboxing(valueCB.getName());
            if (unboxing != null) {
                valueCB = unboxing;
            }
        }

        if (valueCB instanceof PrimitiveTypeBinding) {

            Primitive vtype = ((PrimitiveTypeBinding) valueCB).type;
            if (type == null) {
                return false;
            }

            switch (type) {
                case Boolean:
                    return vtype == Primitive.Boolean;
                case Byte:
                    return vtype == Primitive.Byte;
                case Short:
                    return vtype == Primitive.Byte || vtype == Primitive.Short;
                case Char:
                    return vtype == Primitive.Byte || vtype == Primitive.Short || vtype == Primitive.Char;
                case Int:
                    return vtype == Primitive.Byte || vtype == Primitive.Short || vtype == Primitive.Char || vtype == Primitive.Int;
                case Long:
                    return vtype == Primitive.Byte || vtype == Primitive.Short || vtype == Primitive.Char || vtype == Primitive.Int || vtype == Primitive.Long;
                case Float:
                    return vtype == Primitive.Byte || vtype == Primitive.Short || vtype == Primitive.Char || vtype == Primitive.Int || vtype == Primitive.Long || vtype == Primitive.Float;
                case Double:
                    return vtype == Primitive.Byte || vtype == Primitive.Short || vtype == Primitive.Char || vtype == Primitive.Int || vtype == Primitive.Long || vtype == Primitive.Float || vtype == Primitive.Double;
                default:
                    throw new IllegalStateException(vtype.toString());
            }
        }

        return false;
    }

    @Override
    public boolean isSpecialAssignableFrom(Binding valueCB) {

        if (valueCB instanceof AbstractClassBinding) {
            PrimitiveTypeBinding unboxing = getUnboxing(valueCB.getName());
            if (unboxing != null) {
                valueCB = unboxing;
            }
        }

        if (valueCB instanceof PrimitiveTypeBinding) {

            Primitive vtype = ((PrimitiveTypeBinding) valueCB).type;
            Object vvalue = ((PrimitiveTypeBinding) valueCB).value;
            boolean ret = false;
            switch (type) {
                case Byte:
                    ret = (vtype == Primitive.Int && vvalue != null && (byte) ((Integer) vvalue).intValue() == ((Integer) vvalue).intValue());
                    break;
                case Short:
                    ret = (vtype == Primitive.Int && vvalue != null && (short) ((Integer) vvalue).intValue() == ((Integer) vvalue).intValue());
                    break;
                case Char:
                    ret = (vtype == Primitive.Int && vvalue != null && (char) ((Integer) vvalue).intValue() == ((Integer) vvalue).intValue());
                    break;
                case Float:
                    ret = (vtype == Primitive.Double && vvalue != null && ((Double) vvalue).doubleValue() <= Float.MAX_VALUE && (((Double) vvalue).doubleValue() >= Float.MIN_VALUE || ((Double) vvalue).doubleValue() == 0.0f));
                    break;
            }
            if (ret) {
                return true;
            }
        }
        return super.isSpecialAssignableFrom(valueCB);
    }

    public static PrimitiveTypeBinding getUnboxing(String name) {
        if (unboxingMap == null) {
            unboxingMap = new HashMap<String, PrimitiveTypeBinding>();
            unboxingMap.put("java.lang.Boolean", BooleanType);
            unboxingMap.put("java.lang.Byte", ByteType);
            unboxingMap.put("java.lang.Short", ShortType);
            unboxingMap.put("java.lang.Character", CharType);
            unboxingMap.put("java.lang.Integer", IntType);
            unboxingMap.put("java.lang.Long", LongType);
            unboxingMap.put("java.lang.Float", FloatType);
            unboxingMap.put("java.lang.Double", DoubleType);
        }

        return unboxingMap.get(name);
    }

    public static Binding getBoxing(japa.compiler.Compiler compiler, PrimitiveTypeBinding b) {
        String name;
        switch (b.type) {
            case Boolean:
                name = "Boolean";
                break;
            case Byte:
                name = "Byte";
                break;
            case Short:
                name = "Short";
                break;
            case Char:
                name = "Character";
                break;
            case Int:
                name = "Integer";
                break;
            case Long:
                name = "Long";
                break;
            case Float:
                name = "Float";
                break;
            case Double:
                name = "Double";
                break;
            default:
                throw new IllegalStateException(b.type.toString());
        }

        return compiler.fileManager.getBinding(FileManager.JAVA_LANG, name);
    }

    public PrimitiveTypeBinding mostPreciseType(PrimitiveTypeBinding pr) {
        if (type == Primitive.Double || pr.type == Primitive.Double) {
            return DoubleType;
        }
        if (type == Primitive.Float || pr.type == Primitive.Float) {
            return FloatType;
        }
        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return LongType;
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return IntType;
        }
        if (type == Primitive.Short || pr.type == Primitive.Short) {
            return ShortType;
        }
        if (type == Primitive.Char || pr.type == Primitive.Char) {
            return CharType;
        }
        if (type == Primitive.Byte || pr.type == Primitive.Byte) {
            return ByteType;
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public boolean isInteger() {
        return this.type == Primitive.Byte || this.type == Primitive.Short || this.type == Primitive.Char || this.type == Primitive.Int || this.type == Primitive.Long;
    }

    @Override
    public Binding getCommonType(Binding b) {
        if (type == null) {
            return this;
        }

        if (b instanceof AbstractFieldBinding) {
            b = ((EncapsulatedBinding) b).getType();
        }
        if (b instanceof PrimitiveTypeBinding) {
            PrimitiveTypeBinding pr = (PrimitiveTypeBinding) b;

            if (type == Primitive.Boolean || pr.type == Primitive.Boolean) {
                return BooleanType;
            }
            if (type == Primitive.Byte || pr.type == Primitive.Byte) {
                return ByteType;
            }
            if (type == Primitive.Char || pr.type == Primitive.Char) {
                return CharType;
            }
            if (type == Primitive.Short || pr.type == Primitive.Short) {
                return ShortType;
            }
            if (type == Primitive.Int || pr.type == Primitive.Int) {
                return IntType;
            }
            if (type == Primitive.Long || pr.type == Primitive.Long) {
                return LongType;
            }
            if (type == Primitive.Float || pr.type == Primitive.Float) {
                return FloatType;
            }
            if (type == Primitive.Double || pr.type == Primitive.Double) {
                return DoubleType;
            }
        }

        return null;
    }

    public boolean canCast(Binding b) {
        if (b instanceof PrimitiveTypeBinding) {
            Primitive vtype = ((PrimitiveTypeBinding) b).type;
            if (type == Primitive.Boolean) {
                return vtype == Primitive.Boolean;
            }
            return vtype != Primitive.Boolean;
        }
        return false;
    }

    public Binding opPlus(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Double || pr.type == Primitive.Double) {
            return new PrimitiveTypeBinding(l.doubleValue() + r.doubleValue(), Primitive.Double);
        }
        if (type == Primitive.Float || pr.type == Primitive.Float) {
            return new PrimitiveTypeBinding(l.floatValue() + r.floatValue(), Primitive.Float);
        }
        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() + r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() + r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opMinus(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Double || pr.type == Primitive.Double) {
            return new PrimitiveTypeBinding(l.doubleValue() - r.doubleValue(), Primitive.Double);
        }
        if (type == Primitive.Float || pr.type == Primitive.Float) {
            return new PrimitiveTypeBinding(l.floatValue() - r.floatValue(), Primitive.Float);
        }
        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() - r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() - r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opTimes(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Double || pr.type == Primitive.Double) {
            return new PrimitiveTypeBinding(l.doubleValue() * r.doubleValue(), Primitive.Double);
        }
        if (type == Primitive.Float || pr.type == Primitive.Float) {
            return new PrimitiveTypeBinding(l.floatValue() * r.floatValue(), Primitive.Float);
        }
        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() * r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() * r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opDivide(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Double || pr.type == Primitive.Double) {
            return new PrimitiveTypeBinding(l.doubleValue() / r.doubleValue(), Primitive.Double);
        }
        if (type == Primitive.Float || pr.type == Primitive.Float) {
            return new PrimitiveTypeBinding(l.floatValue() / r.floatValue(), Primitive.Float);
        }
        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() / r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() / r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opRemainder(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Double || pr.type == Primitive.Double) {
            return new PrimitiveTypeBinding(l.doubleValue() % r.doubleValue(), Primitive.Double);
        }
        if (type == Primitive.Float || pr.type == Primitive.Float) {
            return new PrimitiveTypeBinding(l.floatValue() % r.floatValue(), Primitive.Float);
        }
        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() % r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() % r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opOr(PrimitiveTypeBinding pr) {
        if (type == Primitive.Boolean && pr.type == Primitive.Boolean) {
            if (value == null || pr.value == null) {
                return new PrimitiveTypeBinding(null, Primitive.Boolean);
            }
            boolean l = (Boolean) value;
            boolean r = (Boolean) pr.value;
            return new PrimitiveTypeBinding(l | r, Primitive.Boolean);
        }

        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() | r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() | r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opAnd(PrimitiveTypeBinding pr) {
        if (type == Primitive.Boolean && pr.type == Primitive.Boolean) {
            if (value == null || pr.value == null) {
                return new PrimitiveTypeBinding(null, Primitive.Boolean);
            }
            boolean l = (Boolean) value;
            boolean r = (Boolean) pr.value;
            return new PrimitiveTypeBinding(l & r, Primitive.Boolean);
        }

        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() & r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() & r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opXor(PrimitiveTypeBinding pr) {
        if (type == Primitive.Boolean && pr.type == Primitive.Boolean) {
            if (value == null || pr.value == null) {
                return new PrimitiveTypeBinding(null, Primitive.Boolean);
            }
            boolean l = (Boolean) value;
            boolean r = (Boolean) pr.value;
            return new PrimitiveTypeBinding(l ^ r, Primitive.Boolean);
        }

        if (value == null || pr.value == null) {
            return mostPreciseType(pr);
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Long || pr.type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() ^ r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int || pr.type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() ^ r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opLShift(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return this;
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() << r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() << r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opSShift(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return this;
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() >> r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() >> r.longValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

    public Binding opUShift(PrimitiveTypeBinding pr) {
        if (value == null || pr.value == null) {
            return this;
        }
        Number l = (Number) value;
        Number r = (Number) pr.value;

        if (type == Primitive.Long) {
            return new PrimitiveTypeBinding(l.longValue() >>> r.longValue(), Primitive.Long);
        }
        if (type == Primitive.Int) {
            return new PrimitiveTypeBinding(l.intValue() >>> r.intValue(), Primitive.Int);
        }
        throw new IllegalStateException(toString() + ", " + pr.toString());
    }

}
