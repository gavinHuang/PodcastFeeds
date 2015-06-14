package net.gavin.android.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import net.gavin.android.PubItem;
import net.gavin.android.XMLParser;

public class XMLParserTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		XMLParser parser = new XMLParser();
		
		FileInputStream fis = new FileInputStream("D:\\kuaipan\\MSNBC-Nightly.xml");
		List<PubItem> items = parser.readXML(fis);
		System.out.println(items.get(0).getUrl());
		System.out.println(items.get(0).getPubDate());
		System.out.println(items.get(0).getLength());
		
		
		fis = new FileInputStream("D:\\kuaipan\\se-radio.xml");
		items = parser.readXML(fis);
		System.out.println(items.get(0).getUrl());
		System.out.println(items.get(0).getPubDate());
		System.out.println(items.get(0).getLength());
	}

}
