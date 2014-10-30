package com.jakway.stringsgen.exception;

@SuppressWarnings("serial")
public class PrefixNotFoundException extends RuntimeException
{
	public PrefixNotFoundException(String message, String prefix)
	{
		super("Error for prefix: "+prefix+".\n"+message);
	}
}
