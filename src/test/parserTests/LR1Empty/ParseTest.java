package parserTests.LR1Empty;

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
        Node root = getRoot("parserTests/resources/LR1EmptyGrammar.xml", "parserTests/resources/LR1EmptyString.txt");
        verifyChildren(root, new String[] {"A","B","c"});
        
        Node t1 = root.getChildren().get(0);
        verifyChildren(t1, new String[] {"a"});
        verifyLexeme(t1, 0, "a");

        Node t2 = root.getChildren().get(1);
        verifyChildren(t2, new String[] {"empty"});
        verifyLexeme(t2, 0, null);
        
        verifyLexeme (root, 2, "c");
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
