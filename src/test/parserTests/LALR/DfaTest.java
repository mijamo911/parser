package parserTests.LALR;

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
import parser.Reducer;
import parser.Symbol;
import parserTests.AutomatonDumper;
import parserTests.DfaTestBase;

//Section 9.7 pp. 301 Fig. 9.34
public class DfaTest extends DfaTestBase {
    public static void main(String[] args) throws Exception {
        new DfaTest().test();
    }

    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR1Grammar.xml");
        AutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 1);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        Reducer reducer = new Reducer();
        dfa = reducer.reduce(dfa, 0);
        new AutomatonDumper().dump(dfa, System.out);
        
        Production s1 = grammar.findProduction("S", new String[] {"E"});
        Production e1 = grammar.findProduction("E", new String[] {"E","-","T"});
        Production e2 = grammar.findProduction("E", new String[] {"T"});
        Production t1 = grammar.findProduction("T", new String[] {"n"});
        Production t2 = grammar.findProduction("T", new String[] {"(","E",")"});

        DfaState state1 = dfa.getStart();
        verifyState(
            state1,
            new Item[] {
                createItem(s1,0,"#"),
                createItem(e1,0,"#"),
                createItem(e1,0,"-"),
                createItem(e2,0,"#"),
                createItem(e2,0,"-"),
                createItem(t1,0,"#"),
                createItem(t1,0,"-"),
                createItem(t2,0,"#"),
                createItem(t2,0,"-"),
            },
            getSymbols(new String[] {"(","T","n","E"})
        );
        
        DfaState state2 = getNeighbor(state1, "T");
        verifyState(
            state2,
            new Item[] {
                createItem(e2,1,"#"),
                createItem(e2,1,"-"),
                createItem(e2,1,")"),
            },
            new Symbol[0]
        );
        
        DfaState state3 = getNeighbor(state1, "n");
        verifyState(
            state3,
            new Item[] {
                createItem(t1,1,"#"),
                createItem(t1,1,"-"),
                createItem(t1,1,")"),
            },
            new Symbol[0]
        );
        
        DfaState state4 = getNeighbor(state1, "E");
        verifyState(
            state4,
            new Item[] {
                createItem(s1,1,"#"),
                createItem(e1,1,"#"),
                createItem(e1,1,"-"),
            },
            getSymbols(new String[] {"-"})
        );
        
        DfaState state6 = getNeighbor(state1, "(");
        verifyState(
            state6,
            new Item[] {
                createItem(t2,1,"#"),
                createItem(t2,1,"-"),
                createItem(t2,1,")"),
                createItem(e1,0,"-"),
                createItem(e1,0,")"),
                createItem(e2,0,"-"),
                createItem(e2,0,")"),
                createItem(t1,0,"-"),
                createItem(t1,0,")"),
                createItem(t2,0,"-"),
                createItem(t2,0,")"),
            },
            getSymbols(new String[] {"(","T","n","E"})
        );
        
        DfaState state7 = getNeighbor(state4, "-");
        verifyState(
            state7,
            new Item[] {
                createItem(e1,2,"#"),
                createItem(e1,2,"-"),
                createItem(e1,2,")"),
                createItem(t1,0,"#"),
                createItem(t1,0,"-"),
                createItem(t1,0,")"),
                createItem(t2,0,"#"),
                createItem(t2,0,"-"),
                createItem(t2,0,")"),
            },
            getSymbols(new String[] {"n","(","T"})
        );
        
        DfaState state8 = getNeighbor(state7, "T");
        verifyState(
            state8,
            new Item[] {
                createItem(e1,3,"#"),
                createItem(e1,3,"-"),
                createItem(e1,3,")"),
            },
            new Symbol[0]
        );
        
        DfaState state9 = getNeighbor(state6, "E");
        verifyState(
            state9,
            new Item[] {
                createItem(t2,2,"#"),
                createItem(t2,2,"-"),
                createItem(t2,2,")"),
                createItem(e1,1,"-"),
                createItem(e1,1,")"),
            },
            getSymbols(new String[] {"-",")"})
        );
        
        DfaState state10 = getNeighbor(state9, ")");
        verifyState(
            state10,
            new Item[] {
                createItem(t2,3,"#"),
                createItem(t2,3,"-"),
                createItem(t2,3,")"),
            },
            new Symbol[0]
        );
    }
}
