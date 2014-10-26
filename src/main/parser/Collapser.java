package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//A utility class for collapsing non-deterministic finite automatons (NFAs)
//into deterministic finite automatons (DFAs). State is handled by internal
//classes (somewhat overprotectively).
public class Collapser {
    public Dfa collapse(Terminal empty, Nfa nfa) {
        Helper helper = new Helper(empty);
        Dfa dfa = helper.collapse(nfa);
        return dfa;
    }
    
    private class Helper {
        private Terminal empty;
        //A DFA state is correlated with a single set (combination) of NFA states.
        //We track the relationship in both directions so that we can easily
        //determine whether a DFA state already exists for a given set of NFA states
        private Map<DfaState,Set<NfaState>> correlations;
        private Map<Set<NfaState>,DfaState> inverseCorrelations;
        private List<DfaState> unprocessed;
        private int nextStateId = 0;
        private Dfa automaton;
        
        public Helper(Terminal empty) {
            this.empty = empty;
            correlations = new HashMap<DfaState,Set<NfaState>>();
            inverseCorrelations = new HashMap<Set<NfaState>,DfaState>();
            unprocessed = new LinkedList<DfaState>();
            automaton = new Dfa();
        }
        
        //Collapses an NFA into a DFA, starting with the DFA's new start state
        //and continuing with any additional states created during processing
        public Dfa collapse(Nfa nfa) {
            NfaState oldStart = nfa.getStart();
            DfaState newStart = createState();
            automaton.setStart(newStart);
            oldStart.copyTo(newStart);
            Set<NfaState> startCorrelation = new HashSet<NfaState>();
            startCorrelation.add(oldStart);
            correlations.put(newStart, startCorrelation);
            inverseCorrelations.put(startCorrelation, newStart);
            unprocessed.add(newStart);

            while (!unprocessed.isEmpty()) {
                DfaState newState = unprocessed.remove(0);
                StateHelper helper = new StateHelper(newState);
                helper.collapse();
            }

            buildLookaheadTrees();

            return automaton;
        }

        //Builds a tree of all possible lookahead sequences.
        //This allows us to restrict the tokenizer to only look for valid tokens
        private void buildLookaheadTrees() {
            for (DfaState state : automaton) {
                LookaheadNode root = state.getLookaheadTree();
                for (Terminals lookahead : state.getReduceItems().keySet()) {
                    mapLookaheadNode(root, lookahead, 0);
                }
                for (Item item : state.getShiftItems()) {
                    for (Terminals lookahead : item.getDotLookahead()) {
                        mapLookaheadNode(root, lookahead, 0);
                    }
                }
            }
        }

        //Adds a sequence of lookahead terminals into the lookahead tree
        private void mapLookaheadNode(LookaheadNode current, Terminals lookahead, int i) {
            if (i >= lookahead.size()) return;
            Terminal t = lookahead.get(i);
            LookaheadNode next = current.get(t);
            if (next == null) {
                next = new LookaheadNode();
                current.put(t, next);
            }
            mapLookaheadNode(next, lookahead, i + 1);
        }
    
        //Centralizes new state creation and related bookkeeping
        private DfaState createState() {
            DfaState state = new DfaState();
            state.setId(nextStateId++);
            automaton.add(state);
            return state;
        }

        //Handles processing for a single dfa state
        private class StateHelper {
            private DfaState newState;
            private Set<State> mapped;
            private Map<Symbol,Set<NfaState>> transitions;

            public StateHelper(DfaState newState) {
                this.newState = newState;
                mapped = new HashSet<State>();
                transitions = new HashMap<Symbol,Set<NfaState>>();
            }

            /*
             * Every dfa state is correlated with a set of nfa states. First we
             * group the nfa states' neighbors by their transition symbol. For
             * each transition symbol we create (or look up) a destination dfa
             * state that is correlated to the set of nfa states for that symbol.
             * A transition on the symbol is then added from the current dfa
             * state to the destination dfa state. If the destination dfa state
             * is new it is added to the queue for further processing.
             */
            public void collapse() {
                Set<NfaState> oldStates = correlations.get(newState);
                for (NfaState oldState : oldStates) {
                    map(oldState);
                }

                processTransitions();
            }

            //Create a new dfa state for each unique transition symbol in the
            //set of reachable nfa states, or look it up if one already exists
            //for a particular set of states. Newly created dfa states are added
            //to the queue for further processing.
            private void processTransitions() {
                for (Map.Entry<Symbol,Set<NfaState>> entry : transitions.entrySet()) {
                    Symbol symbol = entry.getKey();
                    Set<NfaState> states = entry.getValue();
                    DfaState destination = inverseCorrelations.get(states);
                    if (destination == null) {
                        //New set doesn't exist yet, create
                        destination = createState();
                        correlations.put(destination, states);
                        inverseCorrelations.put(states, destination);
                        unprocessed.add(destination);
                    }
                    newState.getTransitions().put(symbol, new Transition<DfaState>(symbol, destination));
                }
            }
            
            //Finds all neighbors of an nfa state and organizes them by
            //their transition symbol. Empty transitions are considered to be
            //transparent.
            private void map(NfaState oldState) {
                if (mapped.contains(oldState)) return;
                mapped.add(oldState);
                oldState.copyTo(newState);

                for (Transition<NfaState> t : oldState.getTransitions()) {
                    Symbol symbol = t.getSymbol();
                    NfaState destination = t.getDestination();
                    if (symbol.equals(empty)) {
                        //Follow empty transitions (they are transparent)
                        map(t.getDestination());
                    } else {
                        Set<NfaState> destinations = transitions.get(symbol);
                        if (destinations == null) {
                            destinations = new HashSet<NfaState>();
                            transitions.put(symbol, destinations);
                        }
                        if (!destinations.contains(destination)) {
                            destinations.add(destination);
                        }
                    }
                }
            }
        }
    }
}
