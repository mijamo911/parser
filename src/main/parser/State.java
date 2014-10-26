package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class State {
    private int id;
    private Set<Item> shiftItems;
    private Map<Terminals, Item> reduceItems;
    
    public State () {
        shiftItems = new HashSet<Item>();
        reduceItems = new HashMap<Terminals, Item>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Set<Item> getShiftItems() {
        return shiftItems;
    }
    
    public Map<Terminals, Item> getReduceItems() {
        return reduceItems;
    }
    
    @Override
    public String toString() {
        return Integer.toString(id);
    }

    public Core getCore(int lookahead) {
        Set<Item> coreShiftItems = new HashSet<Item>();
        for (Item item : shiftItems) {
            coreShiftItems.add(item.trim(lookahead));
        }

        Map<Terminals, Item> coreReduceItems = new HashMap<Terminals, Item>();
        for (Item item : reduceItems.values()) {
            Item trimmed = item.trim(lookahead);
            for (Terminals terminals : trimmed.getDotLookahead())
                coreReduceItems.put(terminals, item);
        }

        Core core = new Core(coreShiftItems, coreReduceItems);
        return core;
    }

    public void copyTo(State other) {
        other.shiftItems.addAll(shiftItems);
        other.reduceItems.putAll(reduceItems);
    }
}
