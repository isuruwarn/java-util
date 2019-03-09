package org.warn.utils.xml;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestXMLParser {

	@Test
	public void test() {
		XMLParser xmlParser = new XMLParser();
		String sourceXML = "src/test/resources/sample.xml";
		Node root = xmlParser.getRootElement(sourceXML);
		NodeList nodes = root.getChildNodes();
		Assert.assertNotNull(nodes);
		Assert.assertTrue( nodes.getLength() > 0 );
		xmlParser.printElementDetails( nodes, 0 );
	}

}
