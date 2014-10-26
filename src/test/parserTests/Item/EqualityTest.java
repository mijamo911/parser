package parserTests.Item;

import org.junit.Test;
import parser.Item;
import parser.Nonterminal;
import parser.Production;
import parser.Terminal;
import static org.junit.Assert.*;

public class EqualityTest {
    @Test
    public void test() {
        Production p = new Production(1);
        Nonterminal lhs = new Nonterminal("X");
        Terminal t1 = new Terminal("a", null);
        Terminal t2 = new Terminal("b", null);
        Terminal t3 = new Terminal("c", null);
        p.setLeftHandSide(lhs);
        p.getRightHandSide().add(t1);
        p.getRightHandSide().add(t2);
        p.getRightHandSide().add(t3);
        Item i1 = new Item(p, 3);
        Item i2 = new Item(p, 3);
        assertEquals(i1, i2);
    }
}
