package parserTests.FilteredTokenizer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.Test;
import parser.FilteredTokenizer;
import parser.Terminal;
import static org.junit.Assert.*;

public class WhitespaceTest {
    @Test
    public void test() {
        String input = " foo  bar ";
        
        Terminal whitespace = new Terminal("whitespace", Pattern.compile(" "));
        Terminal foo = new Terminal("foo", Pattern.compile("foo"));
        Terminal bar = new Terminal("bar", Pattern.compile("bar"));
        
        BufferedReader reader = new BufferedReader(new StringReader(input));
        
        Set<Terminal> filtered = new HashSet<Terminal>();
        filtered.add(whitespace);
        
        Set<Terminal> unfiltered = new HashSet<Terminal>();
        unfiltered.add(foo);
        unfiltered.add(bar);
        
        FilteredTokenizer tokenizer = new FilteredTokenizer(reader, filtered);
        
        assertEquals("Wrong first item.", foo, tokenizer.next(unfiltered).getSymbol());
        assertEquals("Wrong second item.", bar, tokenizer.next(unfiltered).getSymbol());
        assertNull("Did not return null at end of input.", tokenizer.next(unfiltered));
    }
}
