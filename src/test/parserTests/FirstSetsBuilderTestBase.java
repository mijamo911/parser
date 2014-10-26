package parserTests;

import java.io.IOException;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import parser.FirstSets;
import parser.FirstSetsBuilder;
import parser.Grammar;
import parser.GrammarLoader;
import parser.Symbol;
import parser.Terminal;

import parser.Terminals;
import static org.junit.Assert.*;

public class FirstSetsBuilderTestBase {
    protected Grammar grammar;
    protected FirstSets first;
    
    protected void populateFirstSets(String grammarResourceName, int k) throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar(grammarResourceName);
        FirstSetsBuilder builder = new FirstSetsBuilder();
        first = builder.buildFirstSets(grammar, k);
    }
    
    protected void verifyFirstSet(String symbolName, String[] terminalNames) {
        verifyFirstSet(symbolName, wrap(terminalNames));
    }
    
    protected void verifyFirstSet(String symbolName, String[][] terminalNameSets) {
        Symbol symbol = grammar.getSymbols().get(symbolName);
        Set<Terminals> firsts = first.get(symbol);
        assertEquals(symbolName, terminalNameSets.length, firsts.size());
        for (int i = 0; i < terminalNameSets.length; i++) {
            String[] terminalNames = terminalNameSets[i];
            Terminals terminals = new Terminals();
            for (int j = 0; j < terminalNames.length; j++) {
                terminals.add((Terminal) grammar.getSymbols().get(terminalNames[j]));
            }        
            assertTrue("Terminals " + terminals + " not contained in first set of " + symbol, firsts.contains(terminals));
        }
    }

    protected String[][] wrap(String[] s) {
        String[][] array = new String[s.length][];
        for (int i = 0; i < s.length; i++) {
            array[i] = new String[] {s[i]};
        }
        return array;
    }
}
