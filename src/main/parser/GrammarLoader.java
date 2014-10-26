package parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GrammarLoader {
    public Grammar loadGrammar(String resource) throws ParserConfigurationException, SAXException, IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(stream);

        Helper helper = new Helper();
        return helper.parse(document);        
    }
    
    private class Helper {
        private Grammar grammar;
        private Map<String, NodeProcessor> processors;
        private Map<String, Symbol> knownSymbols;
        private List<UnresolvedProduction> unresolvedProductions;
        private SortedMap<Integer, Terminals> buckets;
        
        private XPathExpression lhsQuery;
        private XPathExpression rhsQuery;
        private String startSymbolName;
        private String whitespaceSymbolName;
        
        public Helper() {
            processors = new HashMap<String, NodeProcessor>();
            knownSymbols = new HashMap<String, Symbol>();
            unresolvedProductions = new LinkedList<UnresolvedProduction>();
            buckets = new TreeMap<Integer, Terminals>();

            NodeProcessor startProcessor = new NodeProcessor() {
                public void process(Node node) {
                    processStartElement(node);
                }
            };
            
            NodeProcessor endProcessor = new NodeProcessor() {
                public void process(Node node) {
                    processEndElement(node);
                }
            };
            
            NodeProcessor whitespaceProcessor = new NodeProcessor() {
                public void process(Node node) {
                    processWhitespaceElement(node);
                }
            };

            NodeProcessor productionProcessor = new NodeProcessor() {
                public void process(Node node) {
                    processProductionElement(node);
                }
            };

            NodeProcessor terminalProcessor = new NodeProcessor() {
                public void process(Node node) {
                    processTerminalElement(node);
                }
            };
            
            NodeProcessor emptyProcessor = new NodeProcessor() {
                public void process(Node node) {
                    processEmptyElement(node);
                }
            };
            
            processors.put("start", startProcessor);
            processors.put("end", endProcessor);
            processors.put("whitespace", whitespaceProcessor);
            processors.put("production", productionProcessor);
            processors.put("terminal", terminalProcessor);
            processors.put("empty", emptyProcessor);

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            try {
                lhsQuery = xpath.compile("lhs");
            } catch (XPathExpressionException wtf) {}
            
            try {
                rhsQuery = xpath.compile("rhs/symbol");
            } catch (XPathExpressionException wtf) {}
        }
        
        public Grammar parse(Document document) {
            grammar = new Grammar();

            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String name = node.getNodeName();
                    NodeProcessor processor = processors.get(name);
                    if (processor == null) {
                        throw new RuntimeException("Unrecognized node name \"" + name + "\"");
                    }
                    processor.process(node);
                }
            }
            
            for (UnresolvedProduction unresolved : unresolvedProductions) {
                Production p = createProduction(unresolved);
                grammar.add(p);
            }
            
            for (Terminals bucket : buckets.values()) {
                grammar.getBuckets().add(bucket);
            }
            
            grammar.setStart(knownSymbols.get(startSymbolName));
            grammar.setWhitespace((Terminal) knownSymbols.get(whitespaceSymbolName));
            
            for (Map.Entry<String,Symbol> entry : knownSymbols.entrySet()) {
                Symbol s = entry.getValue();
                grammar.getSymbols().put(entry.getKey(), s);
                if (s instanceof Terminal) grammar.getTerminals().add((Terminal) s);
            }
            
            return grammar;
        }

        private void processStartElement(Node node) {
            startSymbolName = node.getTextContent();
        }
        
        private void processEndElement(Node node) {
            String name = node.getTextContent();
            Terminal end = new Terminal(name, null);
            grammar.setEnd(end);
            knownSymbols.put(name, end);
        }
        
        private void processEmptyElement(Node node) {
            String name = node.getTextContent();
            Terminal empty = new Terminal(name, null);
            grammar.setEmpty(empty);
            knownSymbols.put(name, empty);
        }
        
        private void processWhitespaceElement(Node node) {
            whitespaceSymbolName = node.getTextContent();
        }
        
        private void processProductionElement(Node node) {
            Node idNode = node.getAttributes().getNamedItem("id");
            if (idNode == null) throw new RuntimeException("Missing id element on production node.");
            int id = Integer.parseInt(idNode.getNodeValue());
            
            Node lhsNode = null;
            try {
                lhsNode = (Node) lhsQuery.evaluate(node, XPathConstants.NODE);
            } catch (XPathExpressionException wtf) {}
            String lhs = lhsNode.getTextContent();
            
            List<String> rhs = new LinkedList<String>();
            NodeList symbols = null;
            try {
                symbols = (NodeList) rhsQuery.evaluate(node, XPathConstants.NODESET);
            } catch (XPathExpressionException wtf) {}
            for (int i = 0; i < symbols.getLength(); i++) {
                Node symbol = symbols.item(i);
                String symbolName = symbol.getTextContent();
                rhs.add(symbolName);
            }
            
            if (!knownSymbols.containsKey(lhs)) {
                Nonterminal n = new Nonterminal(lhs);
                knownSymbols.put(lhs, n);
            }
            unresolvedProductions.add(new UnresolvedProduction(id, lhs, rhs));
        }
        
        private void processTerminalElement(Node node) {
            String name = node.getAttributes().getNamedItem("name").getNodeValue();
            String regex = node.getTextContent();
            Node bucket = node.getAttributes().getNamedItem("bucket");
            Integer bucketId = (bucket == null) ? 1 : Integer.parseInt(bucket.getNodeValue());
            Pattern pattern = Pattern.compile(regex);
            Terminal terminal = new Terminal(name, pattern);
            knownSymbols.put(name, terminal);
            addToBucket(bucketId, terminal);
        }
        
        private Production createProduction(UnresolvedProduction u) {
            int id = u.getId();
            Nonterminal lhs = (Nonterminal) knownSymbols.get(u.getLeftHandSide());
            List<String> rhs = u.getRightHandSide();
            
            Production production = new Production(id);
            production.setLeftHandSide(lhs);
            for (String rhsName : rhs) {
                Symbol s = knownSymbols.get(rhsName);
                if (s == null) throw new RuntimeException("Undefined symbol " + rhsName);
                if (s.equals(grammar.getEmpty())) {
                    lhs.setNullable(true);
                    continue;
                }
                production.getRightHandSide().add(s);
            }
            
            if (production.getRightHandSide().isEmpty()) {
                production.getRightHandSide().add(grammar.getEmpty());
                production.setNull(true);
            }
            
            return production;
        }
        
        private void addToBucket(Integer bucketId, Terminal terminal) {
            Terminals bucket = buckets.get(bucketId);
            if (bucket == null) {
                bucket = new Terminals();
                buckets.put(bucketId, bucket);
            }
            bucket.add(terminal);
        }
    }
    
    private interface NodeProcessor {
        public void process(Node node);
    }
    
    private class UnresolvedProduction {
        private int id;
        private String lhs;
        private List<String> rhs;
        
        public UnresolvedProduction(int id, String lhs, List<String> rhs) {
            this.id = id;
            this.lhs = lhs;
            this.rhs = rhs;
        }
        
        public String getLeftHandSide() {
            return lhs;
        }
        
        public List<String> getRightHandSide() {
            return rhs;
        }

        public int getId() {
            return id;
        }
    }
}
