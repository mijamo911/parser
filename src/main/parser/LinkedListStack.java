package parser;

import java.util.LinkedList;
import java.util.List;

public class LinkedListStack<T> extends LinkedList<T> implements Stack<T> {
    public void push(T o) {
        addFirst(o);
    }

    public T pop() {
        return isEmpty() ? null : removeFirst();
    }
    
    public List<T> peek(int k) {
        if (k < 0 || k >= size()) throw new java.lang.IndexOutOfBoundsException();
        LinkedList<T> list = new LinkedList();
        list.addAll(subList(0, k));
        return list;
    }
}
