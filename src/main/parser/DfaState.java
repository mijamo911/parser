package parser;

import java.util.HashMap;
import java.util.Map;

public class DfaState extends State {
    private Map<Symbol, Transition<DfaState>> transitions;
    private LookaheadNode lookaheadTree;
    
    public DfaState() {
        transitions = new HashMap<Symbol, Transition<DfaState>>();
        lookaheadTree = new LookaheadNode();
    }
    
    public Map<Symbol, Transition<DfaState>> getTransitions() {
        return transitions;
    }
    
    public LookaheadNode getLookaheadTree() {
        return lookaheadTree;
    }
}
