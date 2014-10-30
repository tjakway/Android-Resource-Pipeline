package com.jakway.stringsgen.prefixes;

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
}
