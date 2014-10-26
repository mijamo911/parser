package parser;

import java.util.HashSet;

public abstract class Automaton<T extends State> extends HashSet<T> {
    private T start;
    
    public T getStart() {
        return start;
    }
    
    public void setStart(T start) {
        this.start = start;
    }
}
