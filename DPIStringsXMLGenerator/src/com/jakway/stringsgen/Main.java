package com.jakway.stringsgen;

import java.io.File;
import java.io.IOException;

import com.jakway.stringsgen.file.FileChecks;

public class Main
{
	private static final String USAGE="";
	private static final int MIN_ARGS=2, MAX_ARGS=2;
	private static final int EXIT_FAILURE=1;
	
	public static void main(String[] args)
	{
		if(args.length > MAX_ARGS || args.length < MIN_ARGS)
		{
			System.err.println(USAGE);
			System.exit(EXIT_FAILURE);
		}
		
		File in_drawables_folder = new File(args[0]),
				out_drawables_folder = new File(args[1]);
		
		FileChecks.checkDrawableInputFolder(in_drawables_folder);
		
		boolean empty = false;
		try {
			empty = FileChecks.dirIsEmptyWarnHidden(out_drawables_folder);
		}
		catch(IOException e)
		{
			System.err.println("Fatal IO exception while checking if the output directory is empty.  Program will not exit.");
			e.printStackTrace();
			System.exit(EXIT_FAILURE);
		}
		
		if(!empty)
		{
			System.out.println("Output directory is not empty.");
		}
		
	}
}
