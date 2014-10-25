package com.jakway.assetprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jakway.artprocessor.exception.AssetException;

public class AssetFileOps
{
	private static final String[] drawable_names = {
		"drawable-ldpi", "drawable-mdpi", "drawable-hdpi", "drawable-xhdpi",
		"drawable-xxhdpi"
	};
	private static final String HDPI_PREFIX="hdpi", XHDPI_PREFIX="xhdpi", XXHDPI_PREFIX="xxhdpi";
	private static final String[] prefixes = { 
		"ldpi", "mdpi", HDPI_PREFIX, XHDPI_PREFIX, XXHDPI_PREFIX
	};
	
	private static final boolean isDrawableDir(File file)
	{
		return isDrawableDir(file.getName());
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
	 * writes all input files to their corresponding output paths
	 * @param map of <InputFilepaths, OutputFilepaths>
	 * @throws IOException 
	 */
	private static final void writeMappedOutput(Map<File, File> map) throws IOException
	{
		//iterate through the map with a for-each loop
		for(Map.Entry<File, File> cursor : map.entrySet())
		{
			File inputFile = cursor.getKey(),
					outputFile = cursor.getValue();
			try {
			//use Apache Commons IO to copy the file
			FileUtils.copyFile(inputFile, outputFile);
			}
			//write an error message and throw the exception up
			catch(IOException e)
			{
				System.err.println("Error while writing input file: "+inputFile.getName()+" to output file: "+outputFile.getName());
				throw e;
			}
		}
	}
	
	/**
	 * returns the DPI prefix of file or null if file is not a drawable folder
	 * @param file
	 * @return
	 */
	private static final String getPrefix(File file)
	{
		if(!isDrawableDir(file))
			return null;
		
		String name = file.getName();
		for(int i = 0; i < prefixes.length; i++)
		{
			//need to handle hdpi, xhdpi, and xxhdpi specially because endsWith("hdpi") is also true for xxhdpi and xhdpi
			if(name.endsWith(HDPI_PREFIX) && !name.endsWith(XHDPI_PREFIX) && !name.endsWith(XXHDPI_PREFIX))
				return HDPI_PREFIX;
			else if(name.endsWith(XHDPI_PREFIX) && !name.endsWith(XXHDPI_PREFIX))
				return XHDPI_PREFIX;
			//ends with xxhdpi is also true for hdpi and xhdpi so have to return it before the general check all prefixes or the loop hits hdpi before xxhdpi and returns hdpi
			else if(name.endsWith(XXHDPI_PREFIX))
				return XXHDPI_PREFIX;
			
			if(name.endsWith(prefixes[i]))
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
	private static final Map<File, File> generateFileNames(File inputDir, File outputDir)
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
		
		for(File dir : subDirs)
		{
			if(isDrawableDir(dir))
			{
				//the DPI prefix comes from the folder
				String dpiPrefix = getPrefix(dir);
				
				//go through every image inside this drawable folder
				for(File imgFile : Arrays.asList(dir.listFiles()))
				{
					String copyName = dpiPrefix + "_" + imgFile.getName();
					
					//all output files are in a shallow asset directory
					nameMap.put(imgFile, new File(outputDir, copyName));
				}
			}
			//warn for non-drawable folders
			else
			{
				System.out.println("WARNING: "+dir.toString()+" is not a drawable folder and will be ignored!");
			}
		}
		return nameMap;
	}
	
	public static final void copyDrawableFilesToAssets(File inputFolder, File outputFolder) throws IOException
	{
		//generateFileNames will also check that the inputFolder contains drawable folders
		//also warns for non-drawable folders in the input directory
		Map<File, File> map = generateFileNames(inputFolder, outputFolder);
		
		writeMappedOutput(map);
	}
}
