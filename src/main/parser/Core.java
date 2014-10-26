package parser;

import java.util.Map;
import java.util.Set;

public class Core {
    private final Set<Item> shiftItems;
    private final Map<Terminals, Item> reduceItems;

    public Core(Set<Item> shiftItems, Map<Terminals, Item> reduceItems) {
        this.shiftItems = shiftItems;
        this.reduceItems = reduceItems;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Core)) return false;

        Core other = (Core) o;

        return (shiftItems.equals(other.shiftItems) && reduceItems.equals(other.reduceItems));
    }

    @Override
    public int hashCode() {
        return shiftItems.hashCode() ^ reduceItems.hashCode();
    }
}
