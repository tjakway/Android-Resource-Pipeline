package com.jakway.artprocessor.file;

import java.io.File;
import java.util.ArrayList;

import com.jakway.artprocessor.errorhandler.ArtProcessorErrorHandler;
import com.jakway.artprocessor.exception.ArtProcessorException;

public class FileSystemChecker
{
	public static final String SVG_EXTENSION=".svg";
	
	private File inputDirectory=null;
	private ArtProcessorErrorHandler errorHandler = null;
	
	public FileSystemChecker(File inputDirectory, ArtProcessorErrorHandler errorHandler)
	{
		this.inputDirectory = inputDirectory;
		this.errorHandler = errorHandler;
	}
	
	/**
	 * checks the input directory for:
	 * *non-SVG files
	 * *subdirectories
	 * 
	 * calls the ErrorHandler if no SVG files exist in the input directory
	 * @return a list of SVG files in the passed directory, or null if none exist
	 */
	public ArrayList<File> checkFiles()
	{
		if(inputDirectory == null && errorHandler != null)
		{
			errorHandler.fatalError(new ArtProcessorException("Fatal error: inputDirectory (supposed to contain all SVG images) is null!"));
		}
		
		if(containsFolders(inputDirectory))
		{
			errorHandler.warning(new ArtProcessorException("The SVG input directory has subdirectories--THESE WILL BE IGNORED.  Only SVG files in the top level directory will be processed."));
		}
		
		if(containsNonSVGFiles(inputDirectory))
		{
			errorHandler.warning(new ArtProcessorException("The SVG input directory contains non-SVG files--THESE WILL BE IGNORED."));
		}
		
		ArrayList<File> svgFiles = getSVGFiles(inputDirectory);
		if(svgFiles == null || svgFiles.size() < 1)
		{
			errorHandler.error(new ArtProcessorException("The passed input directory contains no SVG files."));
		}
		
		return svgFiles;
	}
	
	/**
	 * returns the file extension or null if no extension exists
	 * @param file
	 * @return
	 */
	private static final String getFileExtension(File file)
	{
		String extension = "";

		int i = file.toString().lastIndexOf('.');
		if (i > 0) {
		    extension = file.toString().substring(i+1);
		}
		
		if(extension.equals(""))
			return null;
		else
			return extension;
	}
	
	/**
	 * returns true if the directory represented by file contains directories
	 * @param dir
	 * @return
	 */
	private static final boolean containsFolders(File dir)
	{
		File[] files = dir.listFiles();
		for(File subFile : files)
		{
			if(subFile.isDirectory())
				return true;
		}
		return false;
	}
	
	private static final boolean hasSVGExtension(File file)
	{
		String extension = getFileExtension(file);
		//if it doesn't have an extension it can't have an SVG extension
		if(extension == null)
			return false;
		else if(extension.equals(SVG_EXTENSION))
			return true;
		else
			return false;
	}
	
	/**
	 * returns an arraylist of svg files in the passed directory or null if none are found
	 * @param file
	 * @return
	 */
	private static final ArrayList<File> getSVGFiles(File file)
	{
		ArrayList<File> svgFilesList = new ArrayList<File>();
		File[] subFiles = file.listFiles();
		for(File thisFile : subFiles)
		{
			if(!thisFile.isDirectory() && hasSVGExtension(thisFile))
				svgFilesList.add(thisFile);
		}
		
		//don't return an empty arraylist--return null if no svg files exist
		if(svgFilesList.size() > 0)
			return svgFilesList;
		else
			return null;
	}
	
	private static final boolean containsNonSVGFiles(File dir)
	{
		ArrayList<File> svgFiles = getSVGFiles(dir);
		if(svgFiles == null)
			return false;
		
		//if the total number of things in it is not equal to the number of svg files, it contains non svg files (return true)
		if(dir.listFiles().length != svgFiles.size())
			return true;
		else
			return false;
	}
}
