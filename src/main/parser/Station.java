package parser;

import java.util.List;

public class Station extends NfaState {
    private Symbol symbol;
    private Terminals lookahead;
    
    public Station(Symbol symbol) {
        this.symbol = symbol;
    }
    
    public Station(Symbol symbol, Terminals lookahead) {
        this(symbol);
        this.lookahead = lookahead;
    }

    public Symbol getSymbol() {
        return symbol;
    }
    
    public Terminals getLookahead() {
        return lookahead;
    }
    
    @Override
    public boolean equals(Object o ) {
        if (o == null || !(o instanceof Station)) return false;
        Station other = (Station) o;
        if (symbol.equals(other.symbol)) {
            return (lookahead == null) ? other.lookahead == null : lookahead.equals(other.lookahead);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 17 + symbol.hashCode();
        hash = hash * 17 + (lookahead == null ? 0 : lookahead.hashCode());
        return hash;
    }
    
    @Override
    public String toString() {
        return symbol + "[" + (lookahead == null ? "" : lookahead) + "]";
    }
}
