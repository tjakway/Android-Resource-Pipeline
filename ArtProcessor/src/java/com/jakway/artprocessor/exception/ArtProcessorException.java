package com.jakway.artprocessor.exception;

/**
 * for any errors, print the stack trace of the exception and THEN throw this exception
 * @author thomasjakway
 *
 */
@SuppressWarnings("serial")
public class ArtProcessorException extends RuntimeException
{
	public ArtProcessorException()
	{
		super();
	}
	
	public ArtProcessorException(String str)
	{
		super(str);
	}
}
