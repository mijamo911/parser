package parserTests;

import java.util.ArrayList;
import org.junit.Test;
import parser.Terminal;

import static org.junit.Assert.*;

public class ArrayListTest {
    @Test
    public void contentsEqual() {
        ArrayList<Terminal> t1 = new ArrayList<Terminal>();
        ArrayList<Terminal> t2 = new ArrayList<Terminal>();
        
        Terminal t = new Terminal("test", null);
        t1.add(t);
        t2.add(t);
        assertTrue(t1.equals(t2));
    }
}
