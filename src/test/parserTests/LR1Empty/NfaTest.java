package parserTests.LR1Empty;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;
import parser.Automaton;
import parser.GrammarLoader;
import parser.LRkAutomatonBuilder;
import parser.Production;
import parser.Station;
import parserTests.AutomatonDumper;

import static org.junit.Assert.*;

//Sec 9.6.1 Fig 9.31 pp 296
public class NfaTest  extends parserTests.NfaTestBase {    
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR1EmptyGrammar.xml");
        builder = new LRkAutomatonBuilder(grammar, 1);
        Automaton automaton = builder.buildAutomaton();
        
        Station stationS = getStation("S","#");
        Station stationAb = getStation("A","b");
        Station stationAc = getStation("A","c");
        Station stationBc = getStation("B","c");
        
        new AutomatonDumper().dump(automaton, System.out);
        assertNotNull(automaton.getStart());
        assertEquals(15, automaton.size());
        
        StationListener[] empty = new StationListener[0];
        
        Production s1 = grammar.findProduction("S", new String[] {"A","B","c"});
        StationListener[] s1Listeners = new StationListener[] {
            new StationListener(0, stationAb),
            new StationListener(0, stationAc)
        };
        verifyProduction(stationS, s1, s1Listeners);
        
        Production a1 = grammar.findProduction("A", new String[] {"a"});
        verifyProduction(stationAb, a1, empty);
        verifyProduction(stationAc, a1, empty);
        
        Production b1 = grammar.findProduction("B", new String[] {"empty"});
        verifyProduction(stationBc, b1, empty);
        
        Production b2 = grammar.findProduction("B", new String[] {"b"});
        verifyProduction(stationBc, b2, empty);
    }
}
