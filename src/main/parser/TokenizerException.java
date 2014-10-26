package parser;

public class TokenizerException extends RuntimeException {
    private int lineNumber;
    private int position;
    private String line;
    
    public TokenizerException(String line, int lineNumber, int position) {
        this.line = line;
        this.lineNumber = lineNumber;
        this.position = position;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getPosition() {
        return position;
    }

    public String getLine() {
        return line;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("Line ");
        s.append(lineNumber);
        s.append("\n");
        s.append(line);
        s.append("\n");
        for (int i = 0; i < position; i++) {
            s.append("^");
        }
        return s.toString();
    }
}
