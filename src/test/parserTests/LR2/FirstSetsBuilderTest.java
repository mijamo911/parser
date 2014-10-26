package parserTests.LR2;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;

import parserTests.FirstSetsBuilderTestBase;

public class FirstSetsBuilderTest extends FirstSetsBuilderTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        populateFirstSets("parserTests/resources/LR2Grammar.xml", 2);
        
        verifyFirstSet("S", new String[][]{{"q", "e"}});
        verifyFirstSet("A", new String[][]{{"q", "e"}});
        verifyFirstSet("B", new String[][]{{"q", "e"}});
        verifyFirstSet("C", new String[][]{{"q"}});
        verifyFirstSet("D", new String[][]{{"q"}});
        verifyFirstSet("E", new String[][]{{"e"}});
    }
 }
