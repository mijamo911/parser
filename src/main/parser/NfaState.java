package parser;

import java.util.LinkedList;
import java.util.List;

public class NfaState extends State {
    private List<Transition<NfaState>> transitions;

    public NfaState() {
        transitions = new LinkedList<Transition<NfaState>>();
    }
    
    public List<Transition<NfaState>> getTransitions() {
        return transitions;
    }
}
