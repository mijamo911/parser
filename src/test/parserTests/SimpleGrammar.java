package parserTests;

import parser.*;
import java.util.LinkedList;
import java.util.List;

/*
 * A => Bd
 * A => f
 * B => C
 * C => e
 */        
import java.util.regex.Pattern;
public class SimpleGrammar extends Grammar {
    public Nonterminal A;
    public Nonterminal B;
    public Nonterminal C;
    public Terminal d;
    public Terminal e;
    public Terminal f;
    
    public SimpleGrammar() {
        A = new Nonterminal("A");
        B = new Nonterminal("B");
        C = new Nonterminal("C");
        d = new Terminal("d", Pattern.compile("d"));
        e = new Terminal("e", Pattern.compile("e"));
        f = new Terminal("f", Pattern.compile("f"));
        
        for (Symbol s : new Symbol[] {A,B,C,d,e,f}) {
            getSymbols().put(s.getName(), s);
        }
        
        List<Symbol> A1 = listFromArray(new Symbol[] {B, d});
        List<Symbol> A2 = listFromArray(new Symbol[] {f});
        
        List<Symbol> B1 = listFromArray(new Symbol[] {C});
        
        List<Symbol> C1 = listFromArray(new Symbol[] {e});
        
        SequentialIdGenerator generator = new SequentialIdGenerator();
        
        Production pA1 = createProduction(generator, A, A1);
        Production pA2 = createProduction(generator, A, A2);
        Production pB1 = createProduction(generator, B, B1);
        Production pC1 = createProduction(generator, C, C1);
        
        add(pA1);
        add(pA2);
        add(pB1);
        add(pC1);
    }
    
    private Production createProduction(SequentialIdGenerator generator, Nonterminal leftHandSide, List<Symbol> rightHandSide) {
        Production p = new Production(generator.nextId());
        p.setLeftHandSide(leftHandSide);
        p.getRightHandSide().addAll(rightHandSide);
        return p;
    }
    
    private List<Symbol> listFromArray(Symbol[] array) {
        List<Symbol> list = new LinkedList<Symbol>();
        for (Symbol symbol : array) {
            list.add(symbol);
        }
        return list;
    }
    
    private class SequentialIdGenerator {
        private long id = 0;
        
        public long nextId() {
            return id++;
        }
    }
}
