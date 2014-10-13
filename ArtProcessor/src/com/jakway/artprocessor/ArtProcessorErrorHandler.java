package com.jakway.artprocessor;

import com.jakway.artprocessor.exception.ArtProcessorException;

/**
 *  * for any errors, print the stack trace of the exception and THEN call this interface
 * @author thomasjakway
 *
 */
public interface ArtProcessorErrorHandler
{
	public void error(ArtProcessorException e);
	public void fatalError(ArtProcessorException e);
	public void warning(ArtProcessorException e);
}
