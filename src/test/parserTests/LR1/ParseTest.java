package parserTests.LR1;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;
import parseTree.Node;
import parser.AutomatonBuilder;
import parser.Collapser;
import parser.Dfa;
import parser.LRk;
import parser.LRkAutomatonBuilder;
import parser.Nfa;
import parser.Parser;
import parserTests.AutomatonDumper;
import parserTests.ParseTestBase;

public class ParseTest extends ParseTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        Node root = getRoot("parserTests/resources/LR1Grammar.xml", "parserTests/resources/LR1String.txt");
        verifyChildren(root, new String[] {"E"});
        
        Node t1 = root.getChildren().get(0);
        verifyChildren(t1, new String[] {"E","-","T"});
        
        Node t2 = t1.getChildren().get(0);
        verifyChildren(t2, new String[] {"E","-","T"});
        
        Node t3 = t2.getChildren().get(0);
        verifyChildren(t3, new String[] {"T"});
        
        Node t4 = t3.getChildren().get(0);
        verifyChildren(t4, new String[] {"n"});
        verifyLexeme(t4, 0, "6");
        
        Node t5 = t2.getChildren().get(2);
        verifyChildren(t5, new String[] {"n"});
        verifyLexeme(t5, 0, "3");
        
        Node t6 = t1.getChildren().get(2);
        verifyChildren(t6, new String[] {"n"});
        verifyLexeme(t6, 0, "1");
    }
    
    protected Parser createParser() {
        AutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 1);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        new AutomatonDumper().dump(dfa, System.out);
        LRk parser = new LRk(grammar, dfa, 1);
        return parser;
    }
}
