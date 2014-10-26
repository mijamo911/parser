package parserTests.LR0;

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

public class NfaTest  extends parserTests.NfaTestBase{
    //Sec 9.5.1 Fig 9.15 pp 281
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar("parserTests/resources/LR0Grammar.xml");
        builder = new LRkAutomatonBuilder(grammar, 0);
        Automaton automaton = builder.buildAutomaton();
        assertNotNull(automaton.getStart());
        new AutomatonDumper().dump(automaton, System.out);
        
        Station stationS = getStation("S");
        Station stationE = getStation("E");
        Station stationT = getStation("T");
        
        Production s1 = grammar.findProduction("S", new String[] {"E","$"});
        StationListener[] s1_listeners = new StationListener[] {new StationListener(0, stationE)};
        verifyProduction(stationS, s1, s1_listeners);
        
        Production e1 = grammar.findProduction("E", new String[] {"E","-","T"});
        StationListener[] e1_listeners = new StationListener[] {new StationListener(0, stationE), new StationListener(2, stationT)};
        verifyProduction(stationE, e1, e1_listeners);
        
        Production e2 = grammar.findProduction("E", new String[] {"T"});
        StationListener[] e2_listeners = new StationListener[] {new StationListener(0, stationT)};
        verifyProduction(stationE, e2, e2_listeners);
        
        Production t1 = grammar.findProduction("T", new String[] {"n"});
        StationListener[] t1_listeners = new StationListener[0];
        verifyProduction(stationT, t1, t1_listeners);
        
        Production t2 = grammar.findProduction("T", new String[] {"(", "E", ")"});
        StationListener[] t2_listeners = new StationListener[] {new StationListener(1, stationE)};
        verifyProduction(stationT, t2, t2_listeners);
    }
}
