package my_utils;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {

    private final XPathFactory xPathfactory = XPathFactory.newInstance();
    private final XPath xpath = xPathfactory.newXPath();
    private final XPathExpression expr;
    
    public static List<Node> getChildsByTagName(Node node, String tagName){
        NodeList elemNodes = node.getChildNodes();
//        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < elemNodes.getLength(); i++) {
            Node e_node = elemNodes.item(i);
        }
        return new ArrayList<Node>();
    }

    public XMLUtil() throws XPathExpressionException {
        this.expr = xpath.compile("/howto/topic[@name='PowerBuilder']/url");
    }
}
