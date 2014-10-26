package parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LRk implements Parser {
    private Grammar grammar;
    private int k;
            
    private DfaState start;
    private Set<ReductionListener> listeners;
    
    public LRk(Grammar grammar, Dfa automaton, int k) {
        this.grammar = grammar;
        this.k = k;
        
        start = automaton.getStart();
        listeners = new HashSet<ReductionListener>();
    }
    
    public void addListener(ReductionListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }
    
    public void removeListener(ReductionListener listener) {
        listeners.remove(listener);
    }
    
    public void parse(TokenizerBuffer input) {
        new Helper(input).parse();
    }
    
    private class Helper {
        private TokenizerBuffer input;
        
        private Stack<DfaState> state;
        private Stack<Token> output;
        
        public Helper(TokenizerBuffer input) {
            this.input = input;
            
            state = new LinkedListStack<DfaState>();
            output = new LinkedListStack<Token>();
            
            state.push(start);
        }
        
        public void parse() {
            while (true) {
                State current = state.peek();
                Terminals lookahead = getLookahead();
                Item item = current.getReduceItems().get(lookahead);
                if (item != null) {
                    Token lhs = new Token(item.getProduction().getLeftHandSide());
                    Token[] rhs = getReductionRightHandSide(item);
                    for (ReductionListener listener : listeners) {
                        listener.onReduction(item, lhs, rhs);
                    }

                    if (grammar.getStart().equals(lhs.getSymbol())) {
                        return;
                    } else {
                        shift(lhs);
                    }
                } else {
                    shift(input.pop());
                }
            }
        }
        
        private void shift(Token next) {
            DfaState current = state.peek();
            Symbol symbol = next.getSymbol();
            Transition<DfaState> t = current.getTransitions().get(symbol);
            if (t != null) {
                state.push(t.getDestination());
                output.push(next);
            } else {
                throw new RuntimeException("Unexpected symbol " + symbol.getName() + " in state " + current + ": " + next.getLexeme());
            }
        }

        private Token[] getReductionRightHandSide(Item item) {
            Token[] reducedChildren;
            if (item.getProduction().isNull()) {
                reducedChildren = new Token[1];
                reducedChildren[0] = new Token(grammar.getEmpty());
            } else {
                int size = item.getProduction().getRightHandSide().size();
                reducedChildren = new Token[size];
                for (int i = size - 1; i >= 0; i--) {
                    reducedChildren[i] = output.pop();
                    state.pop();
                }
            }
            return reducedChildren;
        }
    
        private Terminals getLookahead() {
            input.fill(state.peek().getLookaheadTree());
            Terminals lookahead = new Terminals();
            List<Token> tokens = input.peek(k);
            for (Token token : tokens) {
                Symbol s = token.getSymbol();
                Terminal t = (Terminal) s;
                lookahead.add(t);
            }
            return lookahead;
        }
    }

    public State getStart() {
        return start;
    }
    
    public int getK() {
        return k;
    }
}
