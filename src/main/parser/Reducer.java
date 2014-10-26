package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Reducer {
    public Dfa reduce(Dfa dfa, int lookahead) {
        Helper helper = new Helper(lookahead);
        return helper.reduce(dfa);
    }

    private class Helper {
        private int lookahead;
        private Map<Core, DfaState> cores;
        private Map<DfaState,DfaState> correlations;
        private List<DfaState> unprocessed;
        private Set<DfaState> processed;
        private int nextStateId = 0;
        private Dfa automaton;

        public Helper(int lookahead) {
            this.lookahead = lookahead;
            cores = new HashMap<Core, DfaState>();
            correlations = new HashMap<DfaState,DfaState>();
            unprocessed = new LinkedList<DfaState>();
            processed = new HashSet<DfaState>();
            automaton = new Dfa();
        }

        public Dfa reduce(Dfa dfa) {
            DfaState oldStart = dfa.getStart();
            DfaState newStart = createState();
            automaton.setStart(newStart);
            oldStart.copyTo(newStart);
            correlations.put(oldStart, newStart);
            unprocessed.add(oldStart);

            while (!unprocessed.isEmpty()) {
                DfaState oldState = unprocessed.remove(0);
                if (processed.contains(oldState)) continue;
                processed.add(oldState);

                DfaState newState = correlations.get(oldState);

                for (Transition<DfaState> transition : oldState.getTransitions().values()) {
                    DfaState neighbor = transition.getDestination();
                    Core core = neighbor.getCore(lookahead);
                    DfaState existing = cores.get(core);
                    if (existing == null) {
                        existing = createState();
                        cores.put(core, existing);
                    }
                    neighbor.copyTo(existing);
                    correlations.put(neighbor, existing);
                    Symbol symbol = transition.getSymbol();
                    newState.getTransitions().put(symbol, new Transition<DfaState>(symbol, existing));
                    unprocessed.add(neighbor);
                }
            }

            return automaton;
        }

        private DfaState createState() {
            DfaState state = new DfaState();
            state.setId(nextStateId++);
            automaton.add(state);
            return state;
        }
    }
}
