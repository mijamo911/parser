package parserTests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import parseTree.Node;
import parseTree.ReductionListener;
import parser.Grammar;
import parser.GrammarLoader;
import parser.Parser;
import parser.TokenizerBuffer;
import parser.Tokenizer;
        
import static org.junit.Assert.*;

public abstract class ParseTestBase {
    protected Grammar grammar;
    
    protected Node getRoot(String grammarResourceName, String inputResourceName) throws ParserConfigurationException, SAXException, IOException {
        GrammarLoader loader = new GrammarLoader();
        grammar = loader.loadGrammar(grammarResourceName);
        Parser parser = createParser();
        ReductionListener listener = new ReductionListener();
        parser.addListener(listener);
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputResourceName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Tokenizer tokenizer = createTokenizer(reader);
        TokenizerBuffer input = new TokenizerBuffer(tokenizer, grammar, parser.getK());
        parser.parse(input);
        return listener.getLast();
    }

    protected Tokenizer createTokenizer(BufferedReader reader) {
        return new Tokenizer(reader);
    }
    
    protected void verifyChildren(Node node, String[] children) {
        assertEquals(children.length, node.getChildren().size());
        for (int i = 0; i < children.length; i++) {
            assertEquals(grammar.getSymbols().get(children[i]), node.getChildren().get(i).getToken().getSymbol());
        }
    }
    
    protected void verifyLexeme(Node parent, int childIndex, String expected) {
        assertEquals(expected, parent.getChildren().get(childIndex).getToken().getLexeme());
    }
    
    protected abstract Parser createParser();
}
