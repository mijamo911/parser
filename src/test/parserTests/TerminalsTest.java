package parserTests;

import org.junit.Test;
import parser.Terminal;
import parser.Terminals;

import static org.junit.Assert.*;

public class TerminalsTest {
    @Test
    public void equality() {
        String name = "NAME";
        Terminal t1 = new Terminal(name, null);
        Terminal t2 = new Terminal(name, null);
        
        Terminals ts1 = new Terminals();
        Terminals ts2 = new Terminals();
        ts1.add(t1);
        ts2.add(t2);
        
        assertTrue("Terminals were not equal.", t1.equals(t2));
        assertTrue("Terminal lists were not equal.", ts1.equals(ts2));
    }
}
