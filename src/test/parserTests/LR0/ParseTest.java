package parserTests.LR0;

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
        Node root = getRoot("parserTests/resources/LR0Grammar.xml", "parserTests/resources/LR0String.txt");
        verifyChildren(root, new String[] {"E", "$"});
        
        Node t1 = root.getChildren().get(0);
        verifyChildren(t1, new String[] {"E","-","T"});
        
        Node t2 = t1.getChildren().get(0);
        verifyChildren(t2, new String[] {"T"});
        
        Node t3 = t2.getChildren().get(0);
        verifyChildren(t3, new String[] {"n"});
        verifyLexeme(t3, 0, "5");
        
        Node t4 = t1.getChildren().get(2);
        verifyChildren(t4, new String[] {"(", "E", ")"});
        
        Node t5 = t4.getChildren().get(1);
        verifyChildren(t5, new String[] {"E","-","T"});
        
        Node t6 = t5.getChildren().get(0);
        verifyChildren(t6, new String[] {"T"});
        
        Node t7 = t6.getChildren().get(0);
        verifyChildren(t7, new String[] {"n"});
        verifyLexeme(t7, 0, "2");
        
        Node t8 = t5.getChildren().get(2);
        verifyChildren(t8, new String[] {"n"});
        verifyLexeme(t8, 0, "1");
    }
    
    protected Parser createParser() {
        AutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 0);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        new AutomatonDumper().dump(dfa, System.out);
        LRk parser = new LRk(grammar, dfa, 0);
        return parser;
    }
}
