package parserTests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;
import parser.Collapser;
import parser.Dfa;
import parser.Grammar;
import parser.GrammarLoader;
import parser.Item;
import parser.LRkAutomatonBuilder;
import parser.Nfa;
import parser.State;
import parser.Terminal;

import static org.junit.Assert.*;

public class HqlGrammarTest {
    @Test
    public void IsLR1() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        Grammar grammar = loader.loadGrammar("parserTests/resources/HqlGrammar.xml");
        LRkAutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 1);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        
        boolean success = true;
        
        for (State state : dfa) {
            if (hasConflict(state)) {
                System.out.println("State " + state);
                System.out.println("\tAccepting items:");
                for (Item item : state.getReduceItems().values()) {
                    System.out.println("\t\t" + item);
                }
                System.out.println("\tItems:");
                for (Item item : state.getShiftItems()) {
                    System.out.println("\t\t" + item);
                }
                System.out.println(state.toString());
                success = false;
            }
        }
        
        assertTrue(success);
    }
    
    private boolean hasConflict(State state) {
        Map<List<Terminal>, Item> lookaheads = new HashMap<List<Terminal>, Item>();
        
        //Reduce/reduce
        for (Item item : state.getReduceItems().values()) {
            List<Terminal> lookahead = item.getItemLookahead();
            if (lookaheads.containsKey(lookahead)) {
                System.out.println("Reduce/reduce:");
                System.out.println("\t" + lookaheads.get(lookahead));
                System.out.println("\t" + item);
                return true;
            }
            lookaheads.put(lookahead, item);
        }
        
        /*
        //Shift/reduce
        for (Item item : state.getItems()) {
            int index = item.getDot();
            List<Symbol> rhs = item.getProduction().getRightHandSide();
            if (lookaheads.contains(rhs.get(index))) {
                return true;
            }
        }
        */
        
        return false;
    }
}
