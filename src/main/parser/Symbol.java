package parser;

public abstract class Symbol implements Cloneable {
    private String name;
    
    public Symbol(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Symbol)) return false;
        
        Symbol other = (Symbol) o;
        return name.equals(other.name);
    }
    
    @Override
    public Symbol clone() {
        try {
            Symbol copy = (Symbol) super.clone();
            return copy;
        } catch (CloneNotSupportedException wtf) {
            return null;
        }
    }
    
    @Override 
    public String toString () {
        return name;
    }
    
    public abstract boolean isNullable();
    public abstract void setNullable(boolean nullable);
}
