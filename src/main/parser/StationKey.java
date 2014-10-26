package parser;

import java.util.List;

public class StationKey {
    private Symbol symbol;
    private Terminals lookahead;

    public StationKey(Symbol symbol, Terminals lookahead) {
        this.symbol = symbol;
        this.lookahead = lookahead;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Terminals getLookahead() {
        return lookahead;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof StationKey)) return false;
        StationKey other = (StationKey) o;
        return
            symbol.equals(other.symbol) &&
            (lookahead == null) ? other.lookahead == null : lookahead.equals(other.lookahead);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 17 + symbol.hashCode();
        hash = hash * 17 + (lookahead == null ? 0 : lookahead.hashCode());
        return hash;
    }
}
