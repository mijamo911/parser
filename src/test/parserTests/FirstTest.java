package parserTests;

import java.util.ArrayList;
import java.util.List;
import parser.*;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class FirstTest {
    @Test
    public void computesSimpleFirstSet() {
        SimpleGrammar grammar = new SimpleGrammar();
        
        Map<Symbol, Set<Terminals>> firstSets = new FirstSetsBuilder().buildFirstSets(grammar, 1);
        verify (grammar.A.getName(), firstSets.get(grammar.A), wrap(new Symbol[] {grammar.e,grammar.f}));
        verify (grammar.B.getName(), firstSets.get(grammar.B), wrap(new Symbol[] {grammar.e}));
        verify (grammar.C.getName(), firstSets.get(grammar.C), wrap(new Symbol[] {grammar.e}));
    }
    
    private void verify(String name, Set<Terminals> firstSet, List<Symbol>[] contents) {
        assertNotNull(name + "'s first set was null", firstSet);
        assertEquals(name + "'s first set's size was not " + contents.length, contents.length, firstSet.size());
        for (List<Symbol> expected : contents) {
            assertTrue(name + "'s first set did not contain " + expectedString(expected), firstSet.contains(expected));
        }
    }
    
    private String expectedString(List<Symbol> symbols) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < symbols.size(); i++) {
            if (i > 0) builder.append(" ");
            builder.append(symbols.get(i).getName());
        }
        builder.append("]");
        return builder.toString();
    }
    
    private List<Symbol>[] wrap(Symbol[] s) {
        List<Symbol>[] array = (List<Symbol>[]) new List[s.length];
        for (int i = 0; i < s.length; i++) {
            List<Symbol> list = new ArrayList<Symbol>(1);
            list.add(s[i]);
            array[i] = list;
        }
        return array;
    }
}
