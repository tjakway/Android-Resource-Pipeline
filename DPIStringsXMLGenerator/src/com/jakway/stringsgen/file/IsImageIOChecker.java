package com.jakway.stringsgen.file;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * opposite of NotImageIOChecker
 * @author thomasjakway
 */
public class IsImageIOChecker implements IOFileFilter
{
	/**
	 * checks for image files
	 * @param file
	 * @param name
	 * @return true for image files, false for non-image files
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
		//accept all non-SVG image files (any file with an extesnion in image_extensions)
		else
		{
			if(FilenameUtils.isExtension(name, FileChecks.image_extensions))
				return true;
			else
				return false;
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
