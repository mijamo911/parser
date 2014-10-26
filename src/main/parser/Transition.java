package parser;

public class Transition<T extends State> {
    private Symbol symbol;
    private T destination;
    
    public Transition(Symbol symbol, T destination) {
        this.symbol = symbol;
        this.destination = destination;
    }

    public T getDestination() {
        return destination;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
        hash = 17 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Transition)) return false;
        Transition other = (Transition) o;
        return symbol.equals(other.symbol) && destination.equals(other.destination);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(symbol.getName());
        builder.append(" => ");
        builder.append(destination);
        return builder.toString();
    }
}
