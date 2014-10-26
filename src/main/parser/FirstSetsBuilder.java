package parser;

import java.util.HashSet;
import java.util.Set;

public class FirstSetsBuilder {
    public FirstSets buildFirstSets(Grammar grammar, int k) {
        return new Helper(grammar, k).buildFirstSets();
    }
    
    private class Helper {
        private boolean changesWereMade;
        private Grammar grammar;
        private int k;
        private FirstSets first;
        
        public Helper(Grammar grammar, int k) {
            this.grammar = grammar;
            this.k = k;
            first = new FirstSets(k);
            initialize();
        }
        
        //Terminals are mapped to themselves for transparent usage
        private void initialize() {
            for (Symbol symbol : grammar.getSymbols().values()) {
                Set<Terminals> firsts = new HashSet<Terminals>();
                if (symbol instanceof Terminal) {
                    Terminals list = new Terminals();
                    list.add((Terminal) symbol);
                    firsts.add(list);
                }
                first.put(symbol, firsts);
            }
        }
        
        private FirstSets buildFirstSets() {
            Terminals empty = new Terminals();
            
            do {
                changesWereMade = false;
                
                for (Production production : grammar) {
                    process(production.getLeftHandSide(), empty, production, 0);
                }
            } while (changesWereMade);
            
            return first;
        }
        
        private void process(Symbol start, Terminals original, Production p, int index) {
            if (original.size() == k || index == p.getRightHandSide().size()) {
                add (start, original);
                return;
            }
            
            Symbol s = p.getRightHandSide().get(index);
            if (s instanceof Terminal) {
                Terminal t = (Terminal) s;
                if (!t.equals(grammar.getEmpty())) {
                    Terminals joined = original.join(t);
                    process(start, joined, p, index + 1);
                }
            } else {
                //Empty (incomplete) sets are a no-op
                Set<Terminals> firsts = first.get(s);
                for (Terminals t : firsts) {
                    Terminals joined = original.join(t, k);
                    process(start, joined, p, index + 1);
                }
                
                //Handle nullable transparency
                if (s.isNullable()) {
                    process(start, original, p, index + 1);
                }
            }
        }
        
        private void add(Symbol start, Terminals t) {
            Set<Terminals> firsts = first.get(start);
            if (!firsts.contains(t)) {
                firsts.add(t);
                changesWereMade = true;
            }
        }
    }
}
