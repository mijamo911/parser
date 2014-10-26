package parser;

public interface ReductionListener {
    public void onReduction(Item item, Token lhs, Token[] rhs);
}
