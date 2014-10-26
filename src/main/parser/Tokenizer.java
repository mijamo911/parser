package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private BufferedReader reader;
    private String line;
    private Matcher matcher;
    private int lineNumber;
    private int position;
    private Pattern dummy;
    
    private boolean end;
    
    public Tokenizer(BufferedReader reader) {
        this.reader = reader;
        dummy = Pattern.compile(".");
    }
    
    public Token next(Set<Terminal> candidates) {
        try {
            while (line == null || line.length() == 0) {
                if (!readNextLine()) return null;
            }
            for (Terminal t : candidates) {
                if (t.getRegex() == null) continue;
                matcher = matcher.usePattern(t.getRegex());
                if (matcher.lookingAt()) {
                    Token token = new Token(matcher.group(), t, lineNumber, matcher.start());
                    position = matcher.end();
                    matcher.region(position, line.length());
                    if (position == line.length()) readNextLine();
                    return token;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return null;
    }
    
    private boolean readNextLine() throws IOException {
        line = reader.readLine();
        if (line != null) {
            lineNumber++;
            matcher = dummy.matcher(line);
            position = 0;
            return true;
        } else {
            end = true;
            return false;
        }
    }
    
    public boolean atEnd() {
        return end;
    }
    
    public String getLine() {
        return line;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public int getPosition() {
        return position;
    }
}
