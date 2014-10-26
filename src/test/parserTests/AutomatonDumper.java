package parserTests;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import parser.Automaton;
import parser.DfaState;
import parser.Item;
import parser.NfaState;
import parser.State;
import parser.Transition;

public class AutomatonDumper {
    private Set<State> seen;
    private List<State> todo;
    private PrintStream writer;
    
    public AutomatonDumper() {
        seen = new HashSet<State>();
        todo = new LinkedList<State>();
    }
    
    public void dump(Automaton automaton, PrintStream writer) {
        this.writer = writer;
        todo.add(automaton.getStart());
        while(!todo.isEmpty()) {
            State state = todo.remove(0);
            process(state);
        }
    }
    
    private void process(State state) {
        if (seen.contains(state)) return;
        seen.add(state);
        writer.println("State " + state);
        writer.println("\tShift Items:");
        for (Item item : state.getShiftItems()) {
            writer.println("\t\t" + item);
        }
        writer.println("\tReduce Items:");
        for (Item item : state.getReduceItems().values()) {
            writer.println("\t\t" + item);
        }
        if (state instanceof NfaState) {
            handleNfaState((NfaState) state);
        } else if (state instanceof DfaState) {
            handleDfaState((DfaState) state);
        }
    }
    
    private void handleNfaState(NfaState state) {
        writer.println("\tTransitions:");
        for (Transition t : state.getTransitions()) {
            writer.println("\t\t" + t);
            todo.add(t.getDestination());
        }
    }
    
    private void handleDfaState(DfaState state) {
        writer.println("\tTransitions:");
        for (Transition t : state.getTransitions().values()) {
            writer.println("\t\t" + t);
            todo.add(t.getDestination());
        }
    }
}
