package com.jakway.stringsgen.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.jakway.stringsgen.exception.DPIStringsGeneratorException;

public class FileChecks
{
	private static final String[] drawable_names = {
		"drawable-ldpi", "drawable-mdpi", "drawable-hdpi", "drawable-xhdpi",
		"drawable-xxhdpi"
	};
	
	private static final String[] image_extensions = 
		{
		"gif", "jpg", "png", 
		"jpeg" /*less common, but still allowed*/
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
		
		if(!containsDrawableFolders(Arrays.asList(dir.listFiles())))
		{
			throw new DPIStringsGeneratorException("Input folder "+dir.toString()+" does not contain any drawable folders!");
		}
		
		
		
		org.apache.commons.io.FileUtils.listFiles(dir, ioFilter, ioFilter);
		
	}
	
	private static final IOFileFilter ioFilter = new IOFileFilter()
	{
		
		private boolean doCheck(File file, String name)
		{
			//if it's a directory, make sure it's a drawable directory
			if(file.isDirectory()) {
				for(int i = 0; i < drawable_names.length; i++)
				{
					if(file.getName().equals(drawable_names[i]))
						return true;
				}
				return false;
			}
			//accept all non-SVG image files (any file with an extesnion in image_extensions)
			else
			{
				if(FilenameUtils.isExtension(name, image_extensions))
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
				
		};
	
	
	private static final boolean containsDrawableFolders(List<File> files)
	{
		for(File file : files)
		{
			if(isDrawableDir(file))
				return true;
		}
		return false;
	}
	
	/**
	 * simply checks if file matches a predefined list of drawable folder names
	 * @param file
	 * @return
	 */
	private static final boolean isDrawableDir(String file)
	{
		for(int i = 0; i < drawable_names.length; i++)
		{
			if(file.equals(drawable_names[i]))
				return true;
		}
		
		return false;
	}
	
	private static final boolean isDrawableDir(File file)
	{
		return isDrawableDir(file.getName());
	}
}