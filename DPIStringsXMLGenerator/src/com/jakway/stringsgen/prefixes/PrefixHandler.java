package com.jakway.stringsgen.prefixes;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.jakway.stringsgen.exception.PrefixNotFoundException;
import com.jakway.stringsgen.misc.Pair;

public class PrefixHandler
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
	
	public static final String getPrefix(String filename)
	{
		//get everything up to the first underscore
		return filename.split("^[^_]+(?=_)")[0]; //the prefix is on the left side, the non-prefix string is to the right
	}
	
	/**
	 * returns the prefix 
	 * @param prefix
	 * @param map
	 * @return
	 */
	public static final File getFolderForPrefix(String prefix, File out, Map<String, ArrayList<Pair<String, String>>> map)
	{
		for(String key : map.keySet())
		{
			if(prefix.equals(key))
				return new File(out, "values-"+key);
		}
		//this should never happen--we checked that the prefix exists
		throw new PrefixNotFoundException(prefix, "Prefix not found in getFolderForPrefix!");
	}
}
