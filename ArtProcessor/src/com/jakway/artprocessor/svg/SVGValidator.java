package com.jakway.artprocessor.svg;

import java.io.File;
import java.util.ArrayList;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.jakway.artprocessor.errorhandler.ArtProcessorErrorHandler;
import com.jakway.artprocessor.exception.ArtProcessorException;

public class SVGValidator implements org.xml.sax.ErrorHandler
{
	private ArrayList<File> svgFiles=null;
	private ArtProcessorErrorHandler errorHandler=null;
	
	public SVGValidator(ArrayList<File> svgFiles, ArtProcessorErrorHandler errorHandler)
	{
		this.svgFiles = svgFiles;
		this.errorHandler = errorHandler;
	}
	
	public void validateSVGs()
	{
		for(File svgFile : svgFiles)
		{
			validateThisSVG(svgFile);
		}
	}
	
	/**
	 * from http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
	 * @param filename
	 * @return
	 */
	public static String convertToFileURL(File file) {
        String path = file.getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
	}
	
	private void validateThisSVG(File svgFile)
	{
		try {
	    String parser = XMLResourceDescriptor.getXMLParserClassName();
	    SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
	    factory.setValidating(true);
	    factory.setErrorHandler(this);
	    
	    factory.createSVGDocument(convertToFileURL(svgFile));
		}
		catch(Exception e)
		{
			errorHandler.warning(new ArtProcessorException("Exception thrown during SVG validation."));
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void error(SAXParseException e) throws SAXException
	{
		errorHandler.warning(new ArtProcessorException("Non-fatal error during SVG validation."));
		e.printStackTrace(System.out);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException
	{
		errorHandler.warning(new ArtProcessorException("Fatal error during SVG validation."));
		e.printStackTrace(System.out);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException
	{
		errorHandler.warning(new ArtProcessorException("Warning during SVG validation."));
		e.printStackTrace(System.out);		
	}
}