package parserTests.LR2;

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

//Section 9.6.2 pp 297-298
public class DfaTest extends DfaTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR2Grammar.xml");
        AutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 2);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        new AutomatonDumper().dump(dfa, System.out);
        
        Production S1 = grammar.findProduction("S", new String[] {"A","a"});
        Production S2 = grammar.findProduction("S", new String[] {"B","b"});
        Production S3 = grammar.findProduction("S", new String[] {"C","e","c"});
        Production S4 = grammar.findProduction("S", new String[] {"D","e","d"});
        Production A1 = grammar.findProduction("A", new String[] {"q","E"});
        Production B1 = grammar.findProduction("B", new String[] {"q","E"});
        Production C1 = grammar.findProduction("C", new String[] {"q"});
        Production D1 = grammar.findProduction("D", new String[] {"q"});
        Production E1 = grammar.findProduction("E", new String[] {"e"});
        
        DfaState state1 = dfa.getStart();
        verifyState(
            state1,
            new Item[] {
                createItem(S1, 0, new String[] {"#","#"}),
                createItem(S2, 0, new String[] {"#","#"}),
                createItem(S3, 0, new String[] {"#","#"}),
                createItem(S4, 0, new String[] {"#","#"}),
                createItem(A1, 0, new String[] {"a","#"}),
                createItem(B1, 0, new String[] {"b","#"}),
                createItem(C1, 0, new String[] {"e","c"}),
                createItem(D1, 0, new String[] {"e","d"})
            },
            getSymbols(new String[] {"q", "A", "B", "C", "D"})
        );
        
        DfaState state2 = getNeighbor(state1, "q");
        verifyState(
            state2,
            new Item[] {
                createItem(A1, 1, new String[] {"a","#"}),
                createItem(B1, 1, new String[] {"b","#"}),
                createItem(C1, 1, new String[] {"e","c"}),
                createItem(D1, 1, new String[] {"e","d"}),
                createItem(E1, 0, new String[] {"a","#"}),
                createItem(E1, 0, new String[] {"b","#"})
            },
            getSymbols(new String[] {"e","E",})
        );
        
        DfaState state3 = getNeighbor(state2, "e");
        verifyState(
            state3,
            new Item[] {
                createItem(E1, 1, new String[] {"a","#"}),
                createItem(E1, 1, new String[] {"b","#"}),
            },
            new Symbol[0]
        );
        
        DfaState state4 = getNeighbor(state2, "E");
        verifyState(
            state4,
            new Item[] {
                createItem(A1, 2, new String[] {"a","#"}),
                createItem(B1, 2, new String[] {"b","#"}),
            },
            new Symbol[0]
        );
        
        DfaState state5 = getNeighbor(state1, "A");
        verifyState(
            state5,
            new Item[] {
                createItem(S1, 1, new String[] {"#","#"}),
            },
            getSymbols(new String[] {"a"})
        );
        
        DfaState state6 = getNeighbor(state5, "a");
        verifyState(
            state6,
            new Item[] {
                createItem(S1, 2, new String[] {"#","#"}),
            },
            new Symbol[0]
        );
        
        DfaState state7 = getNeighbor(state1, "B");
        verifyState(
            state7,
            new Item[] {
                createItem(S2, 1, new String[] {"#","#"}),
            },
            getSymbols(new String[] {"b"})
        );
        
        DfaState state8 = getNeighbor(state7, "b");
        verifyState(
            state8,
            new Item[] {
                createItem(S2, 2, new String[] {"#","#"}),
            },
            new Symbol[0]
        );
        
        DfaState state9 = getNeighbor(state1, "C");
        verifyState(
            state9,
            new Item[] {
                createItem(S3, 1, new String[] {"#","#"}),
            },
            getSymbols(new String[] {"e"})
        );
        
        DfaState state10 = getNeighbor(state9, "e");
        verifyState(
            state10,
            new Item[] {
                createItem(S3, 2, new String[] {"#","#"}),
            },
            getSymbols(new String[] {"c"})
        );
        
        DfaState state11 = getNeighbor(state10, "c");
        verifyState(
            state11,
            new Item[] {
                createItem(S3, 3, new String[] {"#","#"}),
            },
            new Symbol[0]
        );
        
        DfaState state12 = getNeighbor(state1, "D");
        verifyState(
            state12,
            new Item[] {
                createItem(S4, 1, new String[] {"#","#"}),
            },
            getSymbols(new String[] {"e"})
        );
        
        DfaState state13 = getNeighbor(state12, "e");
        verifyState(
            state13,
            new Item[] {
                createItem(S4, 2, new String[] {"#","#"}),
            },
            getSymbols(new String[] {"d"})
        );
        
        DfaState state14 = getNeighbor(state13, "d");
        verifyState(
            state14,
            new Item[] {
                createItem(S4, 3, new String[] {"#","#"}),
            },
            new Symbol[0]
        );
        
        assertEquals("Wrong number of states", 14, dfa.size());
    }
}
