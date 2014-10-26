package parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//This represents an Early-style item inside a state
//i.e. a production with a dot somewhere in the right hand side
public class Item {
    private Production production;
    private int dot;
    private Terminals itemLookahead;
    private Set<Terminals> dotLookahead;
    
    public Item(Production production, int dot) {
        this(production, dot, new Terminals());
    }
    
    public Item(Production production, int dot, Terminals itemLookahead) {
        this.production = production;
        this.dot = dot;
        this.itemLookahead = itemLookahead;
        dotLookahead = new HashSet<Terminals>();
    }

    public Production getProduction() {
        return production;
    }

    public int getDot() {
        return dot;
    }
    
    public List<Terminal> getItemLookahead() {
        return itemLookahead;
    }
    
    public Set<Terminals> getDotLookahead() {
        return dotLookahead;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item)) return false;
        Item other = (Item) o;
        if (production.equals(other.production) && dot == other.dot) {
            return (itemLookahead == null) ? (other.itemLookahead == null) : itemLookahead.equals(other.itemLookahead);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.production != null ? this.production.hashCode() : 0);
        hash = 17 * hash + this.dot;
        hash = 17 * hash + (this.itemLookahead != null ? this.itemLookahead.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(production.getLeftHandSide().getName());
        builder.append(",");
        builder.append(dot);
        builder.append(" => ");
        boolean first = true;
        for (Symbol s : production.getRightHandSide()) {
            if (first) {
                first = false;
            } else {
                builder.append(" ");
            }
            builder.append(s.getName());
        }
        
        if (itemLookahead != null) {
            builder.append(" ");
            builder.append(itemLookahead);
        }
        
        builder.append(" {");
        first = true;
        for (Terminals t : dotLookahead) {
            if (first) {
                first = false;
            } else {
                builder.append(",");
            }
            builder.append(t);
        }
        builder.append("}");
        
        return builder.toString();
    }
    
    public boolean isReduction() {
        return dot == production.getRightHandSide().size();
    }

    public Item trim(int lookahead) {
        Item item = new Item(production, dot);
        for (Terminals terminals : dotLookahead) {
            item.getDotLookahead().add(terminals.trim(lookahead));
        }
        item.getItemLookahead().addAll(itemLookahead.subList(0, lookahead));
        return item;
    }
}
