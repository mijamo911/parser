package parserTests.LR1;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;

import parserTests.FirstSetsBuilderTestBase;

public class FirstSetsBuilderTest extends FirstSetsBuilderTestBase {
    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException {
        populateFirstSets("parserTests/resources/LR1Grammar.xml", 1);
        
        verifyFirstSet("S", new String[] {"n","("});
        verifyFirstSet("E", new String[] {"n","("});
        verifyFirstSet("T", new String[] {"n","("});
        verifyFirstSet("-", new String[] {"-"});
        verifyFirstSet("n", new String[] {"n"});
        verifyFirstSet("(", new String[] {"("});
        verifyFirstSet(")", new String[] {")"});
    }
 }
