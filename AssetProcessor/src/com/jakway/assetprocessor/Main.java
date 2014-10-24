package com.jakway.assetprocessor;

import java.io.File;

import com.jakway.artprocessor.exception.AssetException;
import com.jakway.assetprocessor.util.ArgsUtils;

public class Main
{
	private static final String USAGE="Arguments accepted:" +
			"\n1. intput directory" +
			"\n2. output directory" +
			"\n3. optional 3rd argument: --overwrite=on will delete the output directory before writing to it.";
	private static final int MAX_NUM_ARGS=3, MIN_NUM_AGS=2;
	private static final int EXIT_FAILURE=1;
	
	private static final void checkDir(File dir)
	{
		if(!dir.exists())
		{
			throw new AssetException("directory"+dir.getName()+" does not exist!");
		}
		else if(!dir.isDirectory())
		{
			throw new AssetException("directory"+dir.getName()+" is not a directory!");
		}
	}
	
	public static void main(String[] args)
	{
		if(args.length > MAX_NUM_ARGS || args.length < MIN_NUM_AGS)
		{
			System.out.println(USAGE);
			System.exit(EXIT_FAILURE);
		}
				
		File in = new File(args[0]), out = new File(args[1]);
		
		ArgsUtils.checkOverwriteOption(args, out, USAGE);
		
		//perform basic checks--AssetFileOps will do more detailed checking
		checkDir(in);
		checkDir(out);
		
		try {
		AssetFileOps.copyDrawableFilesToAssets(in, out);
		}
		catch(Exception e)
		{
			System.err.println("ERROR DETECTED WHILE COPYING FILES.");
			e.printStackTrace();
			System.err.println("Copy aborted.  Existing files have been preserved (nothing was deleted)");
		}
	}
}
