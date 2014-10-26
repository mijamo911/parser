package parser;

import java.util.LinkedList;

public class Terminals extends LinkedList<Terminal> {
    public Terminals join (Terminals other, int k) {
        int limit = Math.min(k, size() + other.size());
        Terminals newList = new Terminals();
        newList.addAll(this);
        newList.addAll(other.subList(0, limit - size()));
        return newList;
    }
    
    public Terminals join (Terminal t) {
        Terminals newList = new Terminals();
        newList.addAll(this);
        newList.add(t);
        return newList;
    }

    public Terminals trim (int size) {
        if (size >= size())
            return this;

        Terminals terminals = new Terminals();
        terminals.addAll(subList(0, size));
        return terminals;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        boolean first = true;
        for (Terminal t : this) {
            if (first) {
                first = false;
            } else {
                s.append(" ");
            }
            s.append(t);
        }
        s.append("]");
        return s.toString();
    }
}
