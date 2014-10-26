package parseTree;

import java.util.LinkedList;
import java.util.List;
import parser.Token;

public class Node {
    private Token token;
    private List<Node> children;
    
    public Node(Token token) {
        this.token = token;
        children = new LinkedList<Node>();
    }

    public List<Node> getChildren() {
        return children;
    }

    public Token getToken() {
        return token;
    }
}
