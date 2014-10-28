package com.jakway.stringsgen.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jakway.stringsgen.misc.Pair;

public class MapWriter
{
	//private static final String 
	
	private File in=null,
			out=null;

	public MapWriter(File in, File out)
	{
		this.in = in;
		this.out = out;
	}
	
	private Document getXMLDOMTree(File valuesDir, ArrayList<Pair<String, String>> elements) throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root = doc.createElement("");
		//http://crunchify.com/java-simple-way-to-write-xml-dom-file-in-java/
		return null;
	}
	
	public void write(Map<String, ArrayList<Pair<String, String>>> map)
	{		
		for(Map.Entry<String, ArrayList<Pair<String, String>>> entry : map.entrySet())
		{
			System.out.println("Map key: "+entry.getKey());
			
			ArrayList<Pair<String, String>> list = entry.getValue();
			for(Pair<String, String> pair : list)
			{
				System.out.println("pair left: "+pair.getLeft()+", pair right: "+pair.getRight());
			}
		}
	}
}