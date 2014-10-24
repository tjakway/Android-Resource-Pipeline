package com.jakway.assetprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jakway.artprocessor.exception.AssetException;

public class AssetFileOps
{
	private static final String[] drawable_names = {
		"drawable-ldpi", "drawable-mdpi", "drawable-hdpi", "drawable-xhdpi",
		"drawable-xxhdpi"
	};
	private static final String[] prefixes = { 
		"ldpi", "mdpi", "hdpi", "xhdpi", "xxhdpi"
	};
	
	public static final boolean isDrawableDir(File file)
	{
		return isDrawableDir(file.getName());
	}
	
	/**
	 * simply checks if file matches a predefined list of drawable folder names
	 * @param file
	 * @return
	 */
	public static final boolean isDrawableDir(String file)
	{
		for(int i = 0; i < drawable_names.length; i++)
		{
			if(file.equals(drawable_names[i]))
				return true;
		}
		
		return false;
	}
	
	public static final boolean containsDrawableFolders(List<File> files)
	{
		for(File file : files)
		{
			if(isDrawableDir(file))
				return true;
		}
		return false;
	}
	
	/**
	 * returns the DPI prefix of file or null if file is not a drawable folder
	 * @param file
	 * @return
	 */
	public static final String getPrefix(File file)
	{
		if(!isDrawableDir(file))
			return null;
		
		String name = file.getName();
		for(int i = 0; i < prefixes.length; i++)
		{
			if(name.equals(prefixes[i]))
				return prefixes[i];
		}
		
		//did not match any prefixes
		return null;
	}
	
	/**
	 * returns a map of <input file, output file>
	 * @param inputDir
	 * @param outputDir
	 * @return
	 */
	public static final Map<File, File> generateFileNames(File inputDir, File outputDir)
	{
		List<File> subDirs = Arrays.asList(inputDir.listFiles());
		
		if(!containsDrawableFolders(subDirs))
			throw new AssetException("The input folder has no drawable folders!");
		
		//remove any files beginning with a period (they're hidden)
		for(File file : subDirs)
		{
			if(file.getName().startsWith("."))
				subDirs.remove(file);
		}
		
		Map<File, File> nameMap = new HashMap<File, File>();
		
		for(File file : subDirs)
		{
			if(isDrawableDir(file))
			{
				String dpiPrefix = getPrefix(file);
				String copyName = dpiPrefix + "_" + file.getName();
				
				//all output files are in a shallow asset directory
				nameMap.put(file, new File(outputDir, copyName));
			}
		}
		return nameMap;
	}
}
