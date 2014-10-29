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
	private static final String ROOT_ELEMENT_NAME="resources",
			STRING_ELEMENT_NAME="string",
			NAME_ATTRIBUTE="name";
			
	
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
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		
		//create the root node
		//Android's strings.xml doesn't use a namespace
		Element root = doc.createElement(ROOT_ELEMENT_NAME);
		doc.appendChild(root);
		
		for(Pair<String, String> pair : elements)
		{
			Element stringElement = doc.createElement(STRING_ELEMENT_NAME);
			//name without DPI is the general name used to reference the DPI-qualified name
			stringElement.setAttribute(NAME_ATTRIBUTE, pair.getLeft());
			
			//this isn't actually a subelement, it's the JAXP's way of setting the value of an element
			//it'll appear as <stringElement name="value_of_pair_getleft">value_of_pair_getright</stringElement>
			stringElement.appendChild(doc.createTextNode(pair.getRight()));
			
			root.appendChild(stringElement);
		}
		
		return doc;
	}
	
	public void write(Map<String, ArrayList<Pair<String, String>>> map)
	{		
		for(Map.Entry<String, ArrayList<Pair<String, String>>> entry : map.entrySet())
		{
			System.out.println("Map key: "+entry.getKey());
			
			ArrayList<Pair<String, String>> list = entry.getValue();
			for(Pair<String, String> pair : list)
			{
				//get the XML tree
				
				System.out.println("pair left: "+pair.getLeft()+", pair right: "+pair.getRight());
			}
		}
	}
}