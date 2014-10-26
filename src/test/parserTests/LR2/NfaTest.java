package parserTests.LR2;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;
import parser.GrammarLoader;
import parser.LRkAutomatonBuilder;
import parser.Nfa;
import parser.Production;
import parser.Station;
import parserTests.AutomatonDumper;
import parserTests.NfaTestBase;

import static org.junit.Assert.*;

//Sec 9.6.2 pp 297-298
public class NfaTest extends NfaTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR2Grammar.xml");
        builder = new LRkAutomatonBuilder(grammar, 2);
        Nfa nfa = builder.buildAutomaton();
        
        assertNotNull(nfa.getStart());
        new AutomatonDumper().dump(nfa, System.out);
        
        Station stationS = getStation("S", new String[] {"#", "#"});
        Station stationAaSharp = getStation("A", new String[] {"a", "#"});
        Station stationEaSharp = getStation("E", new String[] {"a", "#"});
        Station stationBbSharp = getStation("B", new String[] {"b", "#"});
        Station stationEbSharp = getStation("E", new String[] {"b", "#"});
        Station stationCec = getStation("C", new String[] {"e", "c"});
        Station stationDed = getStation("D", new String[] {"e", "d"});
        
        Production S1 = grammar.findProduction("S", new String[] {"A", "a"});
        StationListener[] S1_listeners = new StationListener[] {new StationListener(0, stationAaSharp)};
        verifyProduction(stationS, S1, S1_listeners);
        
        Production S2 = grammar.findProduction("S", new String[] {"B", "b"});
        StationListener[] S2_listeners = new StationListener[] {new StationListener(0, stationBbSharp)};
        verifyProduction(stationS, S2, S2_listeners);
        
        Production S3 = grammar.findProduction("S", new String[] {"C", "e", "c"});
        StationListener[] S3_listeners = new StationListener[] {new StationListener(0, stationCec)};
        verifyProduction(stationS, S3, S3_listeners);
        
        Production S4 = grammar.findProduction("S", new String[] {"D", "e", "d"});
        StationListener[] S4_listeners = new StationListener[] {new StationListener(0, stationDed)};
        verifyProduction(stationS, S4, S4_listeners);
        
        Production A = grammar.findProduction("A", new String[] {"q", "E"});
        StationListener[] A_listeners = new StationListener[] {new StationListener(1, stationEaSharp)};
        verifyProduction(stationAaSharp, A, A_listeners);
        
        Production B = grammar.findProduction("B", new String[] {"q", "E"});
        StationListener[] B_listeners = new StationListener[] {new StationListener(1, stationEbSharp)};
        verifyProduction(stationBbSharp, B, B_listeners);
        
        Production C = grammar.findProduction("C", new String[] {"q"});
        verifyProduction(stationCec, C, new StationListener[0]);
        
        Production D = grammar.findProduction("D", new String[] {"q"});
        verifyProduction(stationDed, D, new StationListener[0]);
        
        Production E = grammar.findProduction("E", new String[] {"e"});
        verifyProduction(stationEaSharp, E, new StationListener[0]);
        verifyProduction(stationEbSharp, E, new StationListener[0]);
        
        assertEquals(35, nfa.size());
    }
}
