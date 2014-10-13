package com.jakway.artprocessor.errorhandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.jakway.artprocessor.exception.ArtProcessorException;

/**
 * Implements the ArtProcessorErrorHandler interface
 * prints errors to stderr
 * exits for:
 * *error
 * *fatalError
 * prints warnings to stdout
 * @author thomasjakway
 *
 */
public class TerminatingArtProcessorErrorHandler implements ArtProcessorErrorHandler, org.xml.sax.ErrorHandler
{
	private static final int EXIT_FAILURE=1;
	private String name=null;
	
	public TerminatingArtProcessorErrorHandler(String name)
	{
		this.name=name;
	}
	
	/**
	 * this method can be overridden by subclasses
	 */
	protected void beforeExit()
	{
		
	}
	
	/**
	 * exit on error
	 * write errors to stderr
	 * @param e
	 */
	private void handleError(ArtProcessorException e)
	{
		beforeExit();
		System.err.println("ERROR IN: "+name);
		e.printStackTrace();
		System.exit(EXIT_FAILURE);
	}

	@Override
	public void error(ArtProcessorException e)
	{
		handleError(e);
	}

	@Override
	public void fatalError(ArtProcessorException e)
	{
		handleError(e);		
	}

	/**
	 * write warnings to stdout, dont exit
	 */
	@Override
	public void warning(ArtProcessorException e)
	{
		System.out.println("WARNING IN: "+name);
		e.printStackTrace(System.out);
	}

	/*
	 * *************************************
	 * BEGIN org.xml.sax.ErrorHandler METHODS
	 * *************************************
	 */
	@Override
	public void error(SAXParseException e) throws SAXException
	{
		e.printStackTrace();
		this.warning(new ArtProcessorException("org.xml.sax.ErrorHandler.error() called in TerminatingArtProcessorErrorHandler"));
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException
	{
		e.printStackTrace();
		this.warning(new ArtProcessorException("org.xml.sax.ErrorHandler.fatalError() called in TerminatingArtProcessorErrorHandler"));
	}

	@Override
	public void warning(SAXParseException e) throws SAXException
	{
		e.printStackTrace();
		this.warning(new ArtProcessorException("org.xml.sax.ErrorHandler.warning() called in TerminatingArtProcessorErrorHandler"));
	}
	
	/*
	 * *************************************
	 * END org.xml.sax.ErrorHandler METHODS
	 * *************************************
	 */
}
