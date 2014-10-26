package parserTests;

import java.util.regex.*;
import org.junit.Test;
import static org.junit.Assert.*;

//Example of how to match a non-keyword identifier

public class RegexTest {
    @Test
    public void nonKeyword() {
        Pattern p = Pattern.compile("^(?!list\\b|unset\\b|quit\\b|exit\\b)\\w+\\b");
        assertFalse("list matched", isMatch(p, "list"));
        assertFalse("unset matched", isMatch(p, "unset"));
        assertFalse("quit matched", isMatch(p, "quit"));
        assertFalse("exit matched", isMatch(p, "exit"));
        assertTrue("quits did not match", isMatch(p, "quits"));
        assertTrue("identifier did not match", isMatch(p, "identifier"));
    }
    
    private boolean isMatch(Pattern p, String input) {
        Matcher matcher = p.matcher(input);
        return matcher.matches();
    }
}
