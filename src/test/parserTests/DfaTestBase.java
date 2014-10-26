package parserTests;

import parser.DfaState;
import parser.Grammar;
import parser.Item;
import parser.Production;
import parser.Symbol;

import parser.Terminal;
import parser.Terminals;
import parser.Transition;
import static org.junit.Assert.*;

public class DfaTestBase {
    protected Grammar grammar;
    
    protected void verifyState(DfaState state, Item[] items, Symbol[] symbols) {
        assertEquals("Wrong number of items", items.length, state.getShiftItems().size() + state.getReduceItems().size());
        for (Item item : items) {
            if (item.getDot() == item.getProduction().getRightHandSide().size()) {
                assertTrue("State " + state + " does not contain accepting item " + item, state.getReduceItems().containsValue(item));
            } else {
                assertTrue("State " + state + " does not contain item " + item, state.getShiftItems().contains(item));
            }
        }
        assertEquals("Wrong number of transitions for state " + state, symbols.length, state.getTransitions().size());
        for (Symbol symbol : symbols) {
            assertTrue("State " + state + " does not contain a transition for symbol " + symbol, state.getTransitions().containsKey(symbol));
        }
    }
    
    protected Symbol[] getSymbols(String[] names) {
        int size = names.length;
        Symbol[] results = new Symbol[size];
        for (int i = 0; i < size; i++) {
            results[i] = grammar.getSymbols().get(names[i]);
        }
        return results;
    }

    protected DfaState getNeighbor(DfaState start, String symbolName) {
        Symbol symbol = grammar.getSymbols().get(symbolName);
        Transition<DfaState> t = start.getTransitions().get(symbol);
        return t.getDestination();
    }
    
    protected Item createItem(Production p, int dot) {
        return createItem(p, dot, new String[0]);
    }
    
    protected Item createItem(Production p, int dot, String lookaheadName) {
        return createItem(p, dot, new String[] {lookaheadName});
    }

    protected Item createItem(Production p, int dot, String[] lookaheadNames) {
        Terminals lookahead = new Terminals();
        for (int i = 0; i < lookaheadNames.length; i++) {
            lookahead.add((Terminal) grammar.getSymbols().get(lookaheadNames[i]));
        }
        Item item = new Item(p, dot, lookahead);
        return item;
    }
}
