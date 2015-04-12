package net.gavin.android.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
		PubItem item = parser.readXML(fis);
		System.out.println(item.getUrl());
		System.out.println(item.getPubDate());
		System.out.println(item.getLength());
		
	}

}
