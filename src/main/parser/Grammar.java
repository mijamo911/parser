package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grammar extends LinkedList<Production> {
    private Symbol start;
    private Terminal end;
    private Terminal empty;
    private Terminal whitespace;
    private List<Terminals> buckets;
    private Map<String,Symbol> symbols;
    private Set<Terminal> terminals;
    
    public Grammar() {
        buckets = new LinkedList<Terminals>();
        symbols = new HashMap<String,Symbol>();
        terminals = new HashSet<Terminal>();
    }

    public Symbol getStart() {
        return start;
    }
    
    public void setStart(Symbol start) {
        this.start = start;
    }

    public Terminal getEnd() {
        return end;
    }
    
    public void setEnd(Terminal end) {
        this.end = end;
    }

    public Terminal getEmpty() {
        return empty;
    }

    public void setEmpty(Terminal empty) {
        this.empty = empty;
    }

    public Terminal getWhitespace() {
        return whitespace;
    }

    public void setWhitespace(Terminal whitespace) {
        this.whitespace = whitespace;
    }
    
    public List<Terminals> getBuckets() {
        return buckets;
    }
    
    public Map<String,Symbol> getSymbols() {
        return symbols;
    }
    
    public Set<Terminal> getTerminals() {
        return terminals;
    }
    
    public Production findProduction(String lhs, String[] rhs) {
        for (Production p : this) {
            if (!p.getLeftHandSide().getName().equals(lhs)) continue;
            if (matchRhs(p, rhs)) return p;
        }
        return null;
    }
    
    private static boolean matchRhs(Production p, String[] rhs) {
        int index = -1;
        for (Symbol symbol : p.getRightHandSide()) {
            index++;
            if (!symbol.getName().equals(rhs[index])) return false;
        }
        return true;
    }
}
