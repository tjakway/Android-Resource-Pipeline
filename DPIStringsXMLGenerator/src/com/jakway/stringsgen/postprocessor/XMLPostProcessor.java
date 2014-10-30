package com.jakway.stringsgen.postprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jakway.stringsgen.exception.DPIStringsGeneratorException;
import com.jakway.stringsgen.misc.Pair;
import com.jakway.stringsgen.prefixes.PrefixHandler;

public class XMLPostProcessor
{
	private static final String DEFAULT_VALUES_FOLDER_NAME="values";
	private static final String TEMP_FILENAME=".xmlpostprocesstemp";
	
	/**
	 * removes the standalone=no or standalone=yes strings from the first line of the passed XML file
	 * @param file
	 * @throws IOException 
	 */
	public static final void removeStandaloneAttribute(File file) throws IOException
	{
		File tempFile = new File(file.getParentFile(), TEMP_FILENAME);
		final String standaloneYes = "standalone=\"yes\"", standaloneNo = "standalone=\"no\"";
		BufferedReader br=null;
		try {
		//check if the first line contains standalone=no or standalone=yes
		//do this before reading the entire file into a string to avoid modifying this later in the file
		//though I seriously doubt any XML file has standalone=no/yes in some middle node...
		br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		if(line == null) //file is empty, obviously cant remove anything from it
			return;
		
		//if it contains neither of those, we're done
		if(!line.contains(standaloneYes) && !line.contains(standaloneNo))
			return;
		br.close();
		br = null;
		
		String xmlFile = FileUtils.readFileToString(file, "UTF-8");
		//assume it only contains one of these
		String standaloneStr = null; //this will be set to whichever string this XML file contains
		if(xmlFile.contains(standaloneNo))
			standaloneStr = standaloneNo;
		else if(xmlFile.contains(standaloneYes))
			standaloneStr = standaloneYes;
		else //should never happen--already checked that it contains one of these
		{
			throw new DPIStringsGeneratorException("Error in XML postprocessing!");
		}
		
		//remove the standalone string
		String cleanedString = xmlFile.replaceFirst(standaloneStr, "");
		//make the UTF encoding line undercase
		cleanedString = xmlFile.replaceFirst("UTF-8", "utf-8");
		//delete whitespace between the last attribute and the end of the line
		StringBuilder builder = new StringBuilder(cleanedString);
		final String utf = "\"utf-8\"";
		int index = builder.indexOf(utf);
		if(index != -1)
		{
			//delete everything between "utf-8" and the second ?
			//since there should only be a space here, it should work
			builder.replace(index+utf.length(), builder.indexOf("?", builder.indexOf("?")+1), ""); //gets the first index of ? and starts searching right after that, which fetches the second ?
			cleanedString = builder.toString();
		}
		
		
		//rewrite the file
		//copy to a temp file in the same directory
		
		FileUtils.copyFile(file, tempFile);
		FileUtils.deleteQuietly(file);
		try {
		FileUtils.write(file, cleanedString); //I think this overwrites, but delete before just to be sure
		}
		//error during write, copy the temp file back
		catch(Exception e)
		{
			FileUtils.copyFile(tempFile, file);
			FileUtils.deleteQuietly(tempFile);
		}
		
		}
		finally
		{
			if(br != null)
				br.close();
			
			//delete the temp file if it exists
			if(tempFile != null && tempFile.exists())
			{
				//should throw an error if we're unable to delete the temp file
				FileUtils.forceDelete(tempFile);
			}
		}
	}
	
	/**
	 * convenience method
	 * intentionally not an override of removeStandaloneAttribute because of the 's' at the end--you have to make it explicit that you're passing an arraylist
	 * removes the standalone=no/yes XML attribute from every file in files
	 * @param files
	 * @throws IOException 
	 */
	public static final void removeStandaloneAttributes(ArrayList<File> files) throws IOException
	{
		for(File xmlFile : files)
		{
			removeStandaloneAttribute(xmlFile);
		}
	}
	
	public static final void copyDefaultDPI(String default_dpi_prefix, File out, Map<String, ArrayList<Pair<String, String>>> map) throws IOException
	{
		//get the folder indicated by the default DPI the user passed
		File folderToCopy = PrefixHandler.getFolderForPrefix(default_dpi_prefix, out, map);
		
		File defaultFolder = new File(out, DEFAULT_VALUES_FOLDER_NAME);
		FileUtils.copyDirectoryToDirectory(folderToCopy, defaultFolder);
	}
}
