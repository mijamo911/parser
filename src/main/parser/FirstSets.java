package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FirstSets extends HashMap<Symbol,Set<Terminals>> {
    private int k;
    
    public FirstSets(int k) {
        this.k = k;
    }
    
    public Set<Terminals> getFirstSets(List<Symbol> symbols) {
        return new Helper(symbols).getFirstSets();
    }
    
    private class Helper {
        private Set<Terminals> firstSets;
        private List<Symbol> symbols;
        
        public Helper(List<Symbol> symbols) {
            this.symbols = symbols;
            firstSets = new HashSet<Terminals>();
        }
        
        public Set<Terminals> getFirstSets() {
            map(new Terminals(), 0);
            return firstSets;
        }
        
        private void map(Terminals partial, int i) {
            if (i == symbols.size()) {
                firstSets.add(partial);
                return;
            }
            
            Symbol s = symbols.get(i);
            for (Terminals first : FirstSets.this.get(s)) {
                Terminals joined = partial.join(first, k);
                if (joined.size() == k) {
                    firstSets.add(joined);
                } else {
                    map(joined, i + 1);
                }
            }
            
            //Handle nullable transparency
            if (s.isNullable()) {
                map(new Terminals(), i + 1);
            }
        }
    }
}
