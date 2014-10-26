package parser;

public class Token {
    private Symbol symbol;
    private String lexeme;
    private int lineNumber;
    private int position;
    
    public Token(Symbol symbol) {
        this.symbol = symbol;
    }
    
    public Token(String lexeme, Symbol symbol, int lineNumber, int position) {
        this.symbol = symbol;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.position = position;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getPosition() {
        return position;
    }
}
