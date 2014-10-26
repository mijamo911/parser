package parser;

import java.util.List;

public interface Stack<T> {
    public void push (T o);
    public T pop();
    public T peek();
    public List<T> peek(int k);
    public boolean isEmpty();
    public void clear();
}
