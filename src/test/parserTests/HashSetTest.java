package parserTests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import parser.Symbol;
import parser.Terminal;
import static org.junit.Assert.*;

public class HashSetTest {
    @Test
    public void contentsEqual() {
        HashSet<Symbol> set1 = new HashSet<Symbol>();
        HashSet<Symbol> set2 = new HashSet<Symbol>();
        Symbol s = new Terminal("test", null);
        set1.add(s);
        set2.add(s);
        assertTrue("Sets were not equal", set1.equals(set2));
    }
    
    @Test
    public void hashesEqual() {
        String name = "test";
        Symbol s = new Terminal(name, null);
        HashSet<Symbol> set1 = new HashSet<Symbol>();
        HashSet<Symbol> set2 = new HashSet<Symbol>();
        set1.add(s);
        set2.add(s);
        HashMap<Set<Symbol>,String> map = new HashMap<Set<Symbol>,String>();
        map.put(set1, name);
        assertEquals("Sets did not hash equally", name, map.get(set2));
    }
}
