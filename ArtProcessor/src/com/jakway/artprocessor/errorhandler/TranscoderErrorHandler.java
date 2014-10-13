package com.jakway.artprocessor.errorhandler;

import java.io.File;

/**
 * Deletes the output directory if an error or fatal error occurs
 * @author thomasjakway
 *
 */
public class TranscoderErrorHandler extends TerminatingArtProcessorErrorHandler
{
	private File outputDir =null;

	public TranscoderErrorHandler(String name, File outputDir)
	{
		super(name);
		this.outputDir = outputDir;
	}
	
	@Override
	protected void beforeExit()
	{
		try
		{
			outputDir.delete();
		}
		catch(Exception e)
		{
			//ignore
		}
	}
}
