package parserTests.LR1Empty;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;
import parser.AutomatonBuilder;
import parser.Collapser;
import parser.Dfa;
import parser.DfaState;
import parser.GrammarLoader;
import parser.Item;
import parser.LRkAutomatonBuilder;
import parser.Nfa;
import parser.Production;
import parser.Symbol;
import parserTests.AutomatonDumper;
import parserTests.DfaTestBase;

import static org.junit.Assert.*;

//Section 9.6.1 Fig 9.31 pp 296
public class DfaTest extends DfaTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR1EmptyGrammar.xml");
        AutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 1);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        new AutomatonDumper().dump(dfa, System.out);
        
        Production s1 = grammar.findProduction("S", new String[] {"A","B","c"});
        Production a1 = grammar.findProduction("A", new String[] {"a"});
        Production b1 = grammar.findProduction("B", new String[] {"b"});
        Production b2 = grammar.findProduction("B", new String[] {"empty"});
        
        DfaState state1 = dfa.getStart();
        verifyState(
            state1,
            new Item[] {
                createItem(s1,0,"#"),
                createItem(a1,0,"c"),
                createItem(a1,0,"b")
            },
            getSymbols(new String[] {"a", "A"})
        );
        
        DfaState state2 = getNeighbor(state1, "a");
        verifyState(
            state2,
            new Item[] {
                createItem(a1,1,"c"),
                createItem(a1,1,"b")
            },
            new Symbol[0]
        );
        
        DfaState state3 = getNeighbor(state1, "A");
        verifyState(
            state3,
            new Item[] {
                createItem(s1,1,"#"),
                createItem(b1,0,"c"),
                createItem(b2,1,"c")
            },
            getSymbols(new String[] {"b","B"})
        );

        DfaState state4 = getNeighbor(state3, "b");
        verifyState(
            state4,
            new Item[] {
                createItem(b1,1,"c")
            },
            new Symbol[0]
        );
        
        DfaState state5 = getNeighbor(state3,"B");
        verifyState(
            state5,
            new Item[] {
                createItem(s1,2,"#")
            },
            getSymbols(new String[] {"c"})
        );

        DfaState state6 = getNeighbor(state5,"c");
        verifyState(
            state6,
            new Item[] {
                createItem(s1,3,"#")
            },
           new Symbol[0]
        );
        
        assertEquals("Wrong number of states", 6, dfa.size());
    }
}
