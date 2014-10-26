package com.jakway.stringsgen.file;

import java.io.File;

import com.jakway.stringsgen.exception.DPIStringsGeneratorException;

public class FileChecks
{
	private static final String[] drawable_names = {
		"drawable-ldpi", "drawable-mdpi", "drawable-hdpi", "drawable-xhdpi",
		"drawable-xxhdpi"
	};
	
	public static final void checkDrawableInputFolder(File dir)
	{
		if(!dir.exists())
		{
			throw new DPIStringsGeneratorException("Input folder "+dir.toString()+" does not exist!");
		}
		
		if(!dir.isDirectory())
		{
			throw new DPIStringsGeneratorException("Input folder "+dir.toString()+" is not a directory!");
		}
		
		
	}
}