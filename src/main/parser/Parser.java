package parser;

public interface Parser {
    public void addListener(ReductionListener listener);
    public void removeListener(ReductionListener listener);
    public void parse(TokenizerBuffer input);
    public int getK();
}
