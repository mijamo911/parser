package parserTests;

import org.junit.Test;
import parser.Terminal;

import static org.junit.Assert.*;

public class ArrayTest {
    @Test
    public void contentsEqual() {
        Terminal t = new Terminal("test", null);
        Terminal[] t1 = new Terminal[] {t};
        Terminal[] t2 = new Terminal[] {t};
        
        assertFalse(t1.equals(t2));
    }
}
