package org.warn.utils.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLParser.class);
	
	public Element getRootElement( String sourceXML ) {
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

	public void printElementDetails( NodeList nodes, int spaces ) {
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

	private String indent(int length) {
		String spaces = " ";
		for (int i = 0; i < length; i++)
			spaces = spaces + " ";
		return spaces;
	}

	public String getValue(Element element, String tagName) {
		String value = null;
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes != null && nodes.getLength() > 0) {
			Element node = (Element) nodes.item(0);
			value = node.getFirstChild().getNodeValue();
		}
		return value;
	}

}
