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
		
		//if it's a directory, make sure it's a drawable directory
		if(file.isDirectory()) {
			for(int i = 0; i < FileChecks.drawable_names.length; i++)
			{
				if(file.getName().equals(FileChecks.drawable_names[i]))
					return false;
			}
			return false;
		}
		//accept all non-SVG image files (any file with an extesnion in image_extensions)
		else
		{
			if(FilenameUtils.isExtension(name, FileChecks.image_extensions))
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
