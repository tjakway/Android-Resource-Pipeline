package com.jakway.artprocessor.errorhandler;

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
public class TerminatingArtProcessorErrorHandler implements ArtProcessorErrorHandler
{
	private static final int EXIT_FAILURE=1;
	private String name=null;
	
	public TerminatingArtProcessorErrorHandler(String name)
	{
		this.name=name;
	}
	
	/**
	 * exit on error
	 * write errors to stderr
	 * @param e
	 */
	private void handleError(ArtProcessorException e)
	{
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
}
