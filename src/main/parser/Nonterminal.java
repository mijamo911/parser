package parser;

public class Nonterminal extends Symbol {
    private boolean nullable;
    
    public Nonterminal(String name) {
        super(name);
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
