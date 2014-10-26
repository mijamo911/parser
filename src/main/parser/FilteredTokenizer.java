package parser;

import java.io.BufferedReader;
import java.util.Set;

public class FilteredTokenizer extends Tokenizer {
    private Set<Terminal> filtered;
    
    public FilteredTokenizer(BufferedReader reader, Set<Terminal> filtered ) {
        super(reader);
        this.filtered = filtered;
    }
    
    @Override
    public Token next(Set<Terminal> candidates) {
        Token t;
        while ((t = super.next(filtered)) != null);
        return super.next(candidates);
    }
}
