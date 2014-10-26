package parserTests.LR2;

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
    public void qea() throws ParserConfigurationException, SAXException, IOException {
        Node root = getRoot(
            "parserTests/resources/LR2Grammar.xml",
            "parserTests/resources/LR2/qea.txt");
        verifyChildren(root, new String[] {"A","a"});
        
        Node A = root.getChildren().get(0);
        verifyChildren(A, new String[] {"q", "E"});
        
        Node E = A.getChildren().get(1);
        verifyChildren(E, new String[] {"e"});
    }
    
    @Test
    public void Cec() throws ParserConfigurationException, SAXException, IOException {
        Node root = getRoot(
            "parserTests/resources/LR2Grammar.xml",
            "parserTests/resources/LR2/qec.txt");
        verifyChildren(root, new String[] {"C","e", "c"});
        
        Node C = root.getChildren().get(0);
        verifyChildren(C, new String[] {"q"});
    }
    
    @Test
    public void Ded() throws ParserConfigurationException, SAXException, IOException {
        Node root = getRoot(
            "parserTests/resources/LR2Grammar.xml",
            "parserTests/resources/LR2/qed.txt");
        verifyChildren(root, new String[] {"D","e","d"});
        
        Node D = root.getChildren().get(0);
        verifyChildren(D, new String[] {"q"});
    }
    
    @Test
    public void qeb() throws ParserConfigurationException, SAXException, IOException {
        Node root = getRoot(
            "parserTests/resources/LR2Grammar.xml",
            "parserTests/resources/LR2/qeb.txt");
        verifyChildren(root, new String[] {"B","b"});
        
        Node B = root.getChildren().get(0);
        verifyChildren(B, new String[] {"q", "E"});
        
        Node E = B.getChildren().get(1);
        verifyChildren(E, new String[] {"e"});
    }
    
    @Override
    protected Parser createParser() {
        AutomatonBuilder builder = new LRkAutomatonBuilder(grammar, 2);
        Nfa nfa = builder.buildAutomaton();
        Collapser collapser = new Collapser();
        Dfa dfa = collapser.collapse(grammar.getEmpty(), nfa);
        new AutomatonDumper().dump(dfa, System.out);
        LRk parser = new LRk(grammar, dfa, 2);
        return parser;
    }
}
