package parserTests;

import parser.AutomatonBuilder;
import parser.Grammar;
import parser.Item;
import parser.NfaState;
import parser.Production;
import parser.State;
import parser.Station;
import parser.StationKey;
import parser.Symbol;
import parser.Terminal;
import parser.Terminals;
import parser.Transition;
import static org.junit.Assert.*;

public class NfaTestBase {
    protected Grammar grammar;
    protected AutomatonBuilder builder;

    private void assertDestinationExists(NfaState state, Symbol symbol, Station station) {
        String error = "No transition on " + symbol + " from " + state + " to station " + station;
        assertTrue(error, destinationExists(state, symbol, station));
    }
    
    private boolean destinationExists(NfaState state, Symbol symbol, State destination) {
        for (Transition<NfaState> t : state.getTransitions()) {
            if (t.getSymbol().equals(symbol)) {
                if (t.getDestination() == destination) return true;
            }
        }
        return false;
    }
    
    protected void verifyProduction(Station station, Production p, StationListener[] listeners) {
        NfaState current = station;
        int size = p.getRightHandSide().size();
        for (int i = 0; i <= size; i++) {
            if (i < size && p.getRightHandSide().get(i).equals(grammar.getEmpty())) continue;
            Item item = new Item(p, i, station.getLookahead());
            current = getAndAssertDestination(current, getSymbol(p, i), item);
            for (StationListener listener : listeners) {
                listener.verify(i, current);
            }
        }
    }
    
    private Symbol getSymbol(Production p, int dot) {
        int index = dot - 1;
        return (index == -1) ? grammar.getEmpty() : p.getRightHandSide().get(index);
    }
    
    private NfaState getAndAssertDestination(NfaState state, Symbol symbol, Item item) {
        NfaState destination = getDestination(state, symbol, item);
        String error = getError(state, symbol, item);
        assertNotNull(error, destination);
        return destination;
    }
    
    private String getError(State state, Symbol symbol, Item item) {
        StringBuilder builder = new StringBuilder();
        builder.append("No transition from ");
        builder.append(state);
        builder.append(" on ");
        builder.append(symbol);
        builder.append(" to ");
        builder.append(item);
        return builder.toString();
    }
    
    private NfaState getDestination(NfaState state, Symbol symbol, Item item) {
        for (Transition<NfaState> t : state.getTransitions()) {
            if (t.getSymbol().equals(symbol)) {
                NfaState destination = t.getDestination();
                for (Item i : destination.getShiftItems()) {
                    if (i.equals(item)) return destination;
                }
                for (Item i : destination.getReduceItems().values()) {
                    if (i.equals(item)) return destination;
                }
            }
        }
        return null;
    }
    
    protected class StationListener {
        private int dot;
        private Station station;
        
        public StationListener(int dot, Station station) {
            this.dot = dot;
            this.station = station;
        }
        
        public void verify(int dot, NfaState state) {
            if (dot != this.dot) return;
            assertDestinationExists(state, grammar.getEmpty(), station);
        }
    }
    
    protected StationKey createStationKey(String symbolName, String[] lookaheadNames) {
        Symbol symbol = grammar.getSymbols().get(symbolName);
        Terminals lookahead = createLookahead(lookaheadNames);
        StationKey key = new StationKey(symbol, lookahead);
        return key;
    }
    
    protected Station getStation(String symbolName) {
        return getStation(symbolName, new String[0]);
    }
    
    protected Station getStation(String symbolName, String lookaheadName) {
        return getStation(symbolName, new String[] {lookaheadName});
    }

    protected Station getStation(String symbolName, String[] lookaheadNames) {
        StationKey key = createStationKey(symbolName, lookaheadNames);
        return builder.getStations().get(key);
    }
    
    private Terminals createLookahead(String[] lookaheadNames) {
        Terminals lookahead = new Terminals();
        for (int i = 0; i < lookaheadNames.length; i++) {
            lookahead.add((Terminal) grammar.getSymbols().get(lookaheadNames[i]));
        }
        return lookahead;
    }
}
