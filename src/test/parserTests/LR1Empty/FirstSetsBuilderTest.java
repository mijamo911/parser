package parserTests.LR1Empty;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;

import parserTests.FirstSetsBuilderTestBase;

public class FirstSetsBuilderTest extends FirstSetsBuilderTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        populateFirstSets("parserTests/resources/LR1EmptyGrammar.xml", 1);
        
        verifyFirstSet("S", new String[] {"a"});
        verifyFirstSet("A", new String[] {"a"});
        verifyFirstSet("B", new String[] {"b"});
        verifyFirstSet("a", new String[] {"a"});
        verifyFirstSet("b", new String[] {"b"});
        verifyFirstSet("c", new String[] {"c"});
    }
}
