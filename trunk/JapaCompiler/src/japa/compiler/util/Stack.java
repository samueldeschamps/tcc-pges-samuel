/*
 * Created on 18/01/2007
 */
package japa.compiler.util;

import java.util.LinkedList;

/**
 * @author Julio Vilmar Gesser
 */
// Do not need to be synchronized, so extends LinkedList instead java.util.Stack (who extends Vector(it is synchronized))
public class Stack<T> extends LinkedList<T> {

    @Override
    public void push(T obj) {
        add(obj);
    }

    @Override
    public T pop() {
        return removeLast();
    }

    @Override
    public T peek() {
        return getLast();
    }

    public T peek(int level) {
        return get(level);
    }

}
