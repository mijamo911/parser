package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LRkAutomatonBuilder implements AutomatonBuilder {
    private Grammar grammar;
    private int k;
    private Map<StationKey,Station> stations;
    private int nextId = 0;
    private Terminals emptyList;
    
    public LRkAutomatonBuilder(Grammar grammar, int k) {
        this.grammar = grammar;
        this.k = k;
        stations = new HashMap<StationKey,Station>();
        emptyList = new Terminals();
        emptyList.add(grammar.getEmpty());
    }
    
    public Nfa buildAutomaton() {
        Nfa result = new Helper().buildAutomaton();
        return result;
    }
    
    public Map<StationKey,Station> getStations() {
        return stations;
    }
    
    private class Helper {
        private Nfa automaton;
        private Map<Symbol,Set<Production>> productionsByLhs;
        private List<Station> unprocessedStations;
        private FirstSets firstSets;
        private Map<NfaState,Set<Terminals>> shiftLookaheads;
        
        private NfaState nextState;
        private List<Symbol> rhs;
        
        public Helper() {
            automaton = new Nfa();
            productionsByLhs = new HashMap<Symbol,Set<Production>>();
            unprocessedStations = new LinkedList<Station>();
            mapProductionsByLhs();
            firstSets = new FirstSetsBuilder().buildFirstSets(grammar, k);
            shiftLookaheads = new HashMap<NfaState,Set<Terminals>>();
        }
        
        public Nfa buildAutomaton() {
            Terminals lookahead = new Terminals();
            for (int i = 0; i < k; i++) lookahead.add(grammar.getEnd());
            Station start = new Station(grammar.getStart(), lookahead);
            unprocessedStations.add(start);
            automaton.setStart(start);
            
            while (!unprocessedStations.isEmpty()) {
                Station station = unprocessedStations.remove(0);
                for (Production p : productionsByLhs.get(station.getSymbol())) {
                    process(p, station);
                }
            }
            
            return automaton;
        }
        
        private void process(Production p, Station station) {
            StationKey key = new StationKey(station.getSymbol(), station.getLookahead());
            stations.put(key, station);
            automaton.add(station);
            
            rhs = p.getRightHandSide();
            NfaState current = station;
            
            for (int i = 0; i <= rhs.size(); i++) {
                //Empty rules
                if (i < rhs.size() && p.isNull()) continue;
                
                Item item = createItem(p,i,station);
                nextState = createState();
                Symbol nextSymbol = (i == 0) ? grammar.getEmpty() : rhs.get(i - 1);
                Transition<NfaState> next = new Transition<NfaState>(nextSymbol, nextState);
                current.getTransitions().add(next);
                
                if (i < rhs.size()) {
                    processShiftItem(item, i);
                } else {
                    processReduceItem(item);
                }
                
                current = nextState;
            }
        }
        
        private void processShiftItem(Item item, int i) {
            nextState.getShiftItems().add(item);
            
            //Handle conflicts
            for (Terminals lookahead : item.getDotLookahead()) {
                if (nextState.getReduceItems().containsKey(lookahead)) {
                    Item existing = nextState.getReduceItems().get(lookahead);
                    String message = buildConflictErrorMessage(ConflictTypes.ShiftReduce, nextState, item, existing);
                    throw new RuntimeException(message);
                }
            }
            
            //Add all dot lookaheads to shift lookahead map
            Set<Terminals> shiftLookahead = shiftLookaheads.get(nextState);
            shiftLookahead.addAll(item.getDotLookahead());

            //Generate stations for all lookaheads
            if (rhs.get(i) instanceof Nonterminal) {
                for (Terminals lookahead : getLookahead(item)) {
                    Station newStation = new Station(rhs.get(i), lookahead);
                    StationKey newKey = new StationKey(newStation.getSymbol(), newStation.getLookahead());
                    if (stations.containsKey(newKey)) {
                        newStation = stations.get(newKey);
                    } else {
                        stations.put(newKey, newStation);
                        unprocessedStations.add(newStation);
                    }
                    Transition<NfaState> t = new Transition<NfaState>(grammar.getEmpty(), newStation);
                    nextState.getTransitions().add(t);
                }
            }
        }
        
        private void processReduceItem(Item item) {
            for (Terminals lookahead : item.getDotLookahead()) {
                //Handle conflicts
                if (nextState.getReduceItems().containsKey(lookahead)) {
                    Item existing = nextState.getReduceItems().get(lookahead);
                    String message = buildConflictErrorMessage(ConflictTypes.ReduceReduce, nextState, item, existing);
                    throw new RuntimeException(message);
                }
                if (shiftLookaheads.get(nextState).contains(lookahead)) {
                    String message = buildConflictErrorMessage(ConflictTypes.ShiftReduce, nextState, item, null);
                    throw new RuntimeException(message);
                }
                
                nextState.getReduceItems().put(lookahead, item);
            }
        }
        
        private String buildConflictErrorMessage(ConflictTypes conflictType, State state, Item current, Item existing) {
            StringBuilder builder = new StringBuilder();
            if (conflictType == ConflictTypes.ShiftReduce) {
                builder.append("Shift/reduce");
            } else if (conflictType == ConflictTypes.ReduceReduce) {
                builder.append("Reduce/reduce");
            }
            builder.append(" conflict in state ");
            builder.append(state);
            builder.append(": ");
            builder.append(current);
            builder.append(", ");
            builder.append(existing);
            String message = builder.toString();
            return message;
        }
                
        private Item createItem(Production p, int dot, Station station) {
            Item item = new Item(p, dot, station.getLookahead());
            calculateDotLookahead(item);
            return item;
        }
        
        private void calculateDotLookahead(Item item) {
            List<Symbol> symbols = new LinkedList<Symbol>();
            if (!item.isReduction()) {
            	//Old
                //symbols.add(item.getProduction().getRightHandSide().get(item.getDot()));
            	//New
            	List<Symbol> rhs = item.getProduction().getRightHandSide();
            	for (int i = item.getDot(); i < rhs.size(); i++) {
            		symbols.add(rhs.get(i));
            	}
            }
            symbols.addAll(item.getItemLookahead());
            item.getDotLookahead().addAll(firstSets.getFirstSets(symbols));
        }
        
        private NfaState createState() {
            NfaState state = new NfaState();
            state.setId(nextId++);
            automaton.add(state);
            shiftLookaheads.put(state, new HashSet<Terminals>());
            return state;
        }
        
        private Set<Terminals> getLookahead(Item item) {
            List<Symbol> symbols = new LinkedList<Symbol>();
            List<Symbol> rhs = item.getProduction().getRightHandSide();
            symbols.addAll(rhs.subList(item.getDot() + 1, rhs.size()));
            symbols.addAll(item.getItemLookahead());
            return firstSets.getFirstSets(symbols);
        }
        
        private void mapProductionsByLhs() {
            for (Production p : grammar) {
                Symbol lhs = p.getLeftHandSide();
                Set<Production> productions = productionsByLhs.get(lhs);
                if (productions == null) {
                    productions = new HashSet<Production>();
                    productionsByLhs.put(lhs, productions);
                }
                productions.add(p);
            }
        }
    }

    private enum ConflictTypes {
        ShiftReduce,
        ReduceReduce
    }
}
