package com.jakway.stringsgen.file;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.jakway.stringsgen.prefixes.PrefixHandler;

/**
 * returns a list of files that shouldn't be there:
 * -files that aren't in image_extensions
 * -directories that aren't drawable folders
 */
public class NotImageIOChecker implements IOFileFilter 
{
	/**
	 * checks for improper files
	 * @param file
	 * @param name
	 * @return false for image files, true for non-image files
	 */
	private boolean doCheck(File file, String name)
	{
		//exclude if it begins with a period (hidden file)
		if(file.getName().startsWith(".") || name.startsWith("."))
			return false;
		
		//there shouldn't be any directories
		if(file.isDirectory()) {
			return false;
		}
		//accept all non-SVG image files (any file with an extension in image_extensions)
		else
		{
			if(FilenameUtils.isExtension(name, PrefixHandler.image_extensions))
				return false;
			else
				return true;
		}
	}
	
	
	@Override
	public boolean accept(File file)
	{
		return doCheck(file, file.getName());
	}

	@Override
	public boolean accept(File dir, String name)
	{
		return doCheck(dir, name);
	}
			
}