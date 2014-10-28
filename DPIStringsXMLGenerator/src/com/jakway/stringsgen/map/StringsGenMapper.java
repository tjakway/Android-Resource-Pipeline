package com.jakway.stringsgen.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;

import com.jakway.stringsgen.file.FileChecks;
import com.jakway.stringsgen.misc.Pair;

public class StringsGenMapper
{
	/**
	 * maps values folder name : <string XML key, string XML value> AKA
	 * values folder name : <filename, dpi_specific_filename>
	 * used to model a one to many relationship between prefixes and filenames
	 */
	private Map<String, ArrayList<Pair<String, String>>> valuesToPair = new HashMap<String, ArrayList<Pair<String, String>>>();
	
	public StringsGenMapper(File in, File out)
	{
		if(FileChecks.hasSubDirs(in))
		{
			System.out.println("WARNING: the input folder "+in.toString()+"  has subdirectories--this tool does not support recursive traversal at this time.");
		}
		
		//initialize the map with empty arraylists
		for(int i = 0; i < FileChecks.prefixes.length; i++)
		{
			valuesToPair.put(FileChecks.prefixes[i], new ArrayList<Pair<String, String>>());
		}
			
		//don't traverse subdirectories
		Collection<File> assetFiles = FileUtils.listFiles(in, HiddenFileFilter.VISIBLE, null);
		
		for(File imageFile : assetFiles)
		{
			String name = imageFile.getName();
			
			//get the prefix
			String prefix = null;
			//need to handle hdpi, xhdpi, and xxhdpi specially because endsWith("hdpi") is also true for xxhdpi and xhdpi
			if(name.startsWith(FileChecks.HDPI_PREFIX) && !name.startsWith(FileChecks.XHDPI_PREFIX) && !name.startsWith(FileChecks.XXHDPI_PREFIX))
				prefix = FileChecks.HDPI_PREFIX;
			else if(name.startsWith(FileChecks.XHDPI_PREFIX) && !name.startsWith(FileChecks.XXHDPI_PREFIX))
				prefix = FileChecks.XHDPI_PREFIX;
			//ends with xxhdpi is also true for hdpi and xhdpi so have to return it before the general check all prefixes or the loop hits hdpi before xxhdpi and returns hdpi
			else if(name.startsWith(FileChecks.XXHDPI_PREFIX))
				prefix = FileChecks.XXHDPI_PREFIX;
			
			for(int i = 0; i < FileChecks.prefixes.length; i++)
			{
			if(name.startsWith(FileChecks.prefixes[i]))
				prefix = FileChecks.prefixes[i];
			}
			
			final int prefixIndex = new String(prefix + "_").length();
			Pair<String, String> pair = new Pair<String, String>(name, name.substring(prefixIndex));
			
			//get the arraylist, add to it, and put it back
			ArrayList<Pair<String, String>> prefixList = valuesToPair.get(prefix);
			prefixList.add(pair);
			valuesToPair.put(prefix, prefixList);
		}
		
	}
	
	public Map<String, ArrayList<Pair<String, String>>> getValuesToPair()
	{
		return valuesToPair;
	}
}
