package com.jakway.stringsgen.map;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;

import com.jakway.stringsgen.exception.DPIStringsGeneratorException;
import com.jakway.stringsgen.file.FileChecks;
import com.jakway.stringsgen.misc.Pair;

public class StringsGenMapper
{
	/**
	 * maps values folder name : drawable-dpi folder name
	 */
	private Map<String, String> valuesToDrawable = new HashMap<String, String>();
	/**
	 * maps values folder name : <string XML key, string XML value> AKA
	 * values folder name : <filename, dpi_specific_filename>
	 */
	private Map<String, Pair<String, String>> valuesToPair = new HashMap<String, Pair<String, String>>();
	
	public StringsGenMapper(File in, File out)
	{
		//set up the valuesToDrawable map
		//put each values-xdpi corresponding to the opposite drawable-xdpi
		for(int i = 0; i < FileChecks.values_names.length; i++)
		{
			valuesToDrawable.put(FileChecks.values_names[0], FileChecks.drawable_names[0]);
		}
		
		//set up the valuesToPair map
		//iterate through every drawable folder in the map
		for(Map.Entry<String, String> cursor : valuesToDrawable.entrySet())
		{
			//the folder will be a subdirectory of the main input folder
			File drawableFolder = new File (in, cursor.getValue());
			//skip any nonexistant folders
			if(!drawableFolder.exists())
				continue;
			if(!drawableFolder.isDirectory())
			{
				throw new DPIStringsGeneratorException(drawableFolder.toString()+" is not a directory, expected a drawable folder!");
			}
			
			//exclude subdirectories from the search
			if(FileChecks.hasSubDirs(drawableFolder))
			{
				System.out.println("WARNING: the drawable folder "+drawableFolder.toString()+"  has subdirectories!  The Android build system does NOT allow drawable subdirectories and these will be ignored by this tool.");
			}
			
			//the files for this drawable folder
			Collection<File> files = FileUtils.listFiles(drawableFolder, FileFileFilter.FILE, null);
			//process each file and add it to the map
			for(File file : files)
			{
				String name = file.getName();
				//check each name against all prefixes
				for(int i = 0; i < FileChecks.prefixes.length; i++)
				{
					if(name.startsWith(FileChecks.prefixes[i]))
					{
						//this would normally be prefixes[i].length() - 1 but we also want to truncate the underscore after the DPI prefix
						String noPrefix = name.substring(FileChecks.prefixes[i].length());
						//cursor.getKey() is the values folder associated with this drawable folder
						valuesToPair.put(cursor.getKey(), new Pair<String, String>(name, noPrefix));
					}
				}
			}
			
		}
	}

	public Map<String, String> getValuesToDrawable()
	{
		return valuesToDrawable;
	}

	public Map<String, Pair<String, String>> getValuesToPair()
	{
		return valuesToPair;
	}
}
