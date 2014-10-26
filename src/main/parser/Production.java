package parser;

import java.util.LinkedList;
import java.util.List;

public class Production {
    private long id;
    private Nonterminal leftHandSide;
    private List<Symbol> rightHandSide;
    private boolean nil;
    
    public Production(long id) {
        this.id = id;
        rightHandSide = new LinkedList<Symbol>();
    }
   
    public long getId() {
        return id;
    }
    
    public boolean isNull() {
        return nil;
    }
    
    public void setNull(boolean nil) {
        this.nil = nil;
    }
    
    public Nonterminal getLeftHandSide() {
        return leftHandSide;
    }
    public void setLeftHandSide(Nonterminal leftHandSide) {
        this.leftHandSide = leftHandSide;
    }
    
    public List<Symbol> getRightHandSide() {
        return rightHandSide;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(leftHandSide);
        s.append(" => ");
        for (Symbol symbol : rightHandSide) {
            s.append(symbol);
            s.append(" ");
        }
        return s.toString();
    }
}
