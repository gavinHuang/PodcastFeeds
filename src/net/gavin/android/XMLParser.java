package net.gavin.android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	public List<PubItem> readXML(InputStream inStream) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		List<PubItem> pubItems = new ArrayList<PubItem>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName("item");
			if (items != null && items.getLength() > 0) {
				for (int index = 0; index < items.getLength(); index++) {
					Node node = items.item(index);
					NodeList children = node.getChildNodes();
					for (int i = 0; i < children.getLength(); i++) {
						Node child = children.item(i);
						if (child.getNodeName().equalsIgnoreCase("enclosure")) {
							Node urlAttr = child.getAttributes().getNamedItem(
									"url");
							if (urlAttr != null) {
								PubItem pubItem = new PubItem();								
								String url = urlAttr.getTextContent();
								pubItem.setUrl(url);

								String fileName = url.substring(url
										.lastIndexOf("/") + 1);
								pubItem.setFileName(fileName);
								pubItems.add(pubItem);
							}
						}
					}
				}
			}
			inStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pubItems;
	}
}
