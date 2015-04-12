package net.gavin.android;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	public PubItem readXML(InputStream inStream) {

		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		PubItem pubItem = new PubItem();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document dom = builder.parse(inStream);
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName("enclosure");	
			if (items.item(0) != null){
				Node node = items.item(0);
				
				Node urlAttr = node.getAttributes().getNamedItem("url");
				if (urlAttr != null) {
					String url = urlAttr.getTextContent();
					pubItem.setUrl(url);
					
					String fileName = url.substring(url.lastIndexOf("/")+1);
					pubItem.setFileName(fileName);
				}
				Node lengthAttr = node.getAttributes().getNamedItem("length");
				if (lengthAttr != null) pubItem.setLength(Long.valueOf(lengthAttr.getTextContent()));	
				
			}
			items = root.getElementsByTagName("pubDate");
			if (items.item(0) != null){
				Node node = items.item(0);
				pubItem.setPubDate(node.getTextContent());
			}
			inStream.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return pubItem;

	}
}
