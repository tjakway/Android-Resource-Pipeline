package com.jakway.stringsgen.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;

import com.jakway.stringsgen.exception.DPIStringsGeneratorException;

public class FileChecks
{
	/**
	 * MUST BE IN THE SAME ORDER AS drawable_names
	 */
	public static final String[] values_names = {
		"values-ldpi", "values-mdpi", "values-hdpi", "values-xhdpi", "values-xxhdpi"
	};
	
	public static final String HDPI_PREFIX="hdpi", XHDPI_PREFIX="xhdpi", XXHDPI_PREFIX="xxhdpi";
	public static final String[] prefixes = { 
		"ldpi", "mdpi", HDPI_PREFIX, XHDPI_PREFIX, XXHDPI_PREFIX
	};
	
	public static final String[] image_extensions = 
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
		
		//get all files that shouldn't be there
		Collection<File> badFiles = org.apache.commons.io.FileUtils.listFiles(dir, new NotImageIOChecker(), null);
		for(File file : badFiles)
		{
			System.err.println("WARNING: found misplaced file "+file.toString()+" in the input folder.  It will be ignored.");
		}
		
	}
	
	/**
	 * you should check if the passed file is a directory before calling this method
	 * warns if the directory contains hidden files but still returns true if it's empty except for hidden files
	 * USES APACHE IO COMMONS
	 * @param file
	 * @return
	 */
	public static final boolean dirIsEmptyWarnHidden(File dir) throws IOException
	{
		if(!dir.isDirectory())
			throw new IOException(dir.toString()+" is not a directory--can't check if it's empty!");
		
		//check for visible files first because if they exist it rules out the directory regardless of hidden files
		Collection<File> visibleFiles = FileUtils.listFiles(dir, HiddenFileFilter.VISIBLE, HiddenFileFilter.VISIBLE);
		
		if(visibleFiles.size() > 0)
			return false;
		else
		{
			//check for hidden files
			Collection<File> hiddenFiles = FileUtils.listFiles(dir, HiddenFileFilter.HIDDEN, HiddenFileFilter.HIDDEN);
			
			if(hiddenFiles.size() > 0)
				System.out.println("WARNING: output directory "+dir.toString()+" contains hidden files.");
			return true;
		}
	}
	
	/**
	 * gets all non hidden files and tests if any of them are directories
	 * @param dir
	 * @return
	 */
	public static final boolean hasSubDirs(File dir)
	{
		if(!dir.isDirectory())
			throw new DPIStringsGeneratorException("Passed file is not a directory!");
		
		Collection<File> files = FileUtils.listFilesAndDirs(dir, HiddenFileFilter.VISIBLE, HiddenFileFilter.VISIBLE);
		
		for(File file : files)
		{
			if(file.isDirectory())
				return true;
		}
		return false;
	}
}