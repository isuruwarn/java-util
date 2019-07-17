package org.warn.utils.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);
	
	private static final String ENCODING = "UTF-8";
	private static final String INDENT = "yes";
	private static final String XPATH_EVAL_ID = "//*[@id='%s']";
	
	public static Element getRootElement( String sourceXML ) {
		Element root = null;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
			File xmlFile = new File(sourceXML);
			Document doc = documentBuilder.parse(xmlFile);
			root = doc.getDocumentElement();
		} catch( ParserConfigurationException | SAXException | IOException e ) {
			LOGGER.error("Error while parsing XML", e);
		}
		return root;
	}
	
	public static synchronized Document parse( String sourceXML ) {
		Document doc = null;
		try {
			File xmlFile = new File(sourceXML);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			//docBuilderFactory.setValidating(true);
			//docBuilderFactory.setNamespaceAware(true);
			//docBuilderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xmlFile);
		}
		catch( ParserConfigurationException | SAXException | IOException e ) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static synchronized boolean update( String sourceXML, Document doc ) {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty( OutputKeys.ENCODING, ENCODING );
			tf.setOutputProperty( OutputKeys.INDENT, INDENT );
			Writer out = new FileWriter( new File(sourceXML) );
			tf.transform( new DOMSource(doc), new StreamResult(out) );
			return true;
		} catch (TransformerException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static synchronized boolean deleteNode( String charCode, Node root ) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node node = (Node) xpath.evaluate( String.format( XPATH_EVAL_ID, charCode ), root, XPathConstants.NODE);
			if( node != null ) { // remove existing node
				Node parent = node.getParentNode();
				parent.removeChild(node);
				return true;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void printElementDetails( NodeList nodes, int spaces ) {
		for( int i = 0; i < nodes.getLength(); i++ ) {
			if( nodes.item(i).getNodeType() == Node.ELEMENT_NODE ) {
				Node n1 = nodes.item(i);
				LOGGER.info( indent(spaces) + n1.getNodeName() + ": " + n1.getChildNodes().item(0).getNodeValue() );
				if( n1.hasChildNodes() ) {
					printElementDetails( n1.getChildNodes(), spaces + 4 );
				}
			}
		}
	}

	private static String indent(int length) {
		String spaces = " ";
		for (int i = 0; i < length; i++)
			spaces = spaces + " ";
		return spaces;
	}

	public static String getValue(Element element, String tagName) {
		String value = null;
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes != null && nodes.getLength() > 0) {
			Element node = (Element) nodes.item(0);
			value = node.getFirstChild().getNodeValue();
		}
		return value;
	}

}