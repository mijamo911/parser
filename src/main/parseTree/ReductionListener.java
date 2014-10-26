package parseTree;

import java.util.HashMap;
import java.util.Map;
import parser.Item;
import parser.Token;

public class ReductionListener implements parser.ReductionListener
{
    private Node last;
    private Map<Token,Node> nodes;
    
    public ReductionListener() {
        nodes = new HashMap<Token,Node>();
    }
    
    public void onReduction(Item item, Token lhs, Token[] rhs) {
        Node node = new Node(lhs);
        nodes.put(lhs, node);
        for (Token t : rhs) {
            Node child = nodes.get(t);
            if (child == null) {
                child = new Node(t);
                nodes.put(t, child);
            }
            node.getChildren().add(child);
        }
        last = node;
    }

    public Node getLast() {
        return last;
    }
}
