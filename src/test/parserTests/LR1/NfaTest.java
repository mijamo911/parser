package parserTests.LR1;

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

//Sec 9.6 Fig 9.26 pp 292
public class NfaTest  extends parserTests.NfaTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR1Grammar.xml");
        builder = new LRkAutomatonBuilder(grammar, 1);
        Automaton automaton = builder.buildAutomaton();
        
        assertNotNull(automaton.getStart());
        new AutomatonDumper().dump(automaton, System.out);
        
        Station stationS = getStation("S","#");
        Station stationESharp = getStation("E","#");
        Station stationTSharp = getStation("T","#");
        Station stationEMinus = getStation("E","-");
        Station stationTMinus = getStation("T","-");
        Station stationEParen = getStation("E",")");
        Station stationTParen = getStation("T",")");
        
        
        Production s1 = grammar.findProduction("S", new String[] {"E"});
        StationListener[] s1_listeners = new StationListener[] {new StationListener(0, stationESharp)};
        verifyProduction(stationS, s1, s1_listeners);

        Production e1 = grammar.findProduction("E", new String[] {"E","-","T"});
        StationListener[] e1SharpListeners = new StationListener[] {new StationListener(0, stationEMinus), new StationListener(2, stationTSharp)};
        StationListener[] e1MinusListeners = new StationListener[] {new StationListener(0, stationEMinus), new StationListener(2, stationTMinus)};
        StationListener[] e1ParenListeners = new StationListener[] {new StationListener(0, stationEMinus), new StationListener(2, stationTParen)};
        verifyProduction(stationESharp, e1, e1SharpListeners);
        verifyProduction(stationEMinus, e1, e1MinusListeners);
        verifyProduction(stationEParen, e1, e1ParenListeners);
        
        Production e2 = grammar.findProduction("E", new String[] {"T"});
        StationListener[] e2SharpListeners = new StationListener[] {new StationListener(0, stationTSharp)};
        StationListener[] e2MinusListeners = new StationListener[] {new StationListener(0, stationTMinus)};
        StationListener[] e2ParenListeners = new StationListener[] {new StationListener(0, stationTParen)};
        verifyProduction(stationESharp, e2, e2SharpListeners);
        verifyProduction(stationEMinus, e2, e2MinusListeners);
        verifyProduction(stationEParen, e2, e2ParenListeners);
        
        Production t1 = grammar.findProduction("T", new String[] {"n"});
        StationListener[] t1Listeners = new StationListener[0];
        verifyProduction(stationTSharp, t1, t1Listeners);
        verifyProduction(stationTMinus, t1, t1Listeners);
        verifyProduction(stationTParen, t1, t1Listeners);
        
        Production t2 = grammar.findProduction("T", new String[] {"(","E",")"});
        StationListener[] t2Listeners = new StationListener[] {new StationListener(1, stationEParen)};
        verifyProduction(stationTSharp, t2, t2Listeners);
        verifyProduction(stationTMinus, t2, t2Listeners);
        verifyProduction(stationTParen, t2, t2Listeners);

        assertEquals(45, automaton.size());
    }
}
