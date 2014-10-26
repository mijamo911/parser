package parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TokenizerBuffer {
    private Tokenizer tokenizer;
    private Grammar grammar;
    private int k;

    private LinkedList<Token> buffer;
    
    public TokenizerBuffer(Tokenizer tokenizer, Grammar grammar, int k) {
        this.tokenizer = tokenizer;
        this.grammar = grammar;
        this.k = k;
        buffer = new LinkedList<Token>();
    }

    public Token pop() {
        return buffer.removeFirst();
    }

    public Token peek() {
        return buffer.getFirst();
    }
    
    public List<Token> peek(int k) {
        List<Token> items = new ArrayList<Token>(k);
        items.addAll(buffer.subList(0, k));
        return items;
    }
    
    public void fill(LookaheadNode node) {
        if (k == 0 && buffer.isEmpty()) {
            //We need to have -something- in the buffer
            Token t = tokenizer.next(grammar.getTerminals());
            if (t == null) {
                if (tokenizer.atEnd()) {
                    fillEnd();
                    return;
                } else {
                    throw new TokenizerException(tokenizer.getLine(), tokenizer.getLineNumber(), tokenizer.getPosition());
                }
            } else {
                buffer.add(t);
            }
            return;
        }
        
        //Shortcuts
        if (buffer.size() == k) return;
        if (tokenizer.atEnd()) {
            fillEnd();
            return;
        }
        
        //Advance lookahead to match buffer contents
        for (int i = 0; i < buffer.size(); i++) {
            Token buffered = buffer.get(i);
            if (buffered.getSymbol().equals(grammar.getEnd())) break;
            node = node.get(buffered.getSymbol());
        }
        
        //Fill buffer with remaining lookahead
        for (int i = buffer.size(); i < k; i++) {
            Token t = tokenizer.next(node.keySet());
            if (t == null) {
                if (tokenizer.atEnd()) {
                    fillEnd();
                    return;
                } else {
                    throw new TokenizerException(tokenizer.getLine(), tokenizer.getLineNumber(), tokenizer.getPosition());
                }
            } else {
                buffer.add(t);
                node = node.get(t.getSymbol());
            }
        }
    }
    
    private void fillEnd() {
        if (k == 0 && buffer.isEmpty()) {
            buffer.add(new Token(grammar.getEnd()));
            return;
        }
        
        for (int i = buffer.size(); i < k; i++) {
            buffer.add(new Token(grammar.getEnd()));
        }
    }
}
