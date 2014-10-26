package parser;

import java.util.regex.Pattern;

public class Terminal extends Symbol {
    private Pattern regex;
    
    public Terminal(String name, Pattern regex) {
        super(name);
        this.regex = regex;
    }
    
    public Pattern getRegex() {
        return regex;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public void setNullable(boolean nullable) {}
}
