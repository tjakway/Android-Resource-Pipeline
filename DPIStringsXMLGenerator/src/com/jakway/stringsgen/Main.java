package com.jakway.stringsgen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jakway.stringsgen.file.FileChecks;
import com.jakway.stringsgen.map.MapWriter;
import com.jakway.stringsgen.map.Mapper;
import com.jakway.stringsgen.misc.Pair;
import com.jakway.stringsgen.options.OptionsHandler;
import com.jakway.stringsgen.postprocessor.XMLPostProcessor;

public class Main
{
	private static final int EXIT_FAILURE=1;
	
	public static void main(String[] args)
	{
		//FIXME: DEBUG
		args = new String[] {"--input", "assets", "--output", "out_folder", "--default_dpi", "hdpi" , "--overwrite", "on"};
		//FIXME: END DEBUG
		
		OptionsHandler optsHandler = new OptionsHandler();
		
		
		File in_drawables_folder=null,
				out_values_parent_folder=null;
		boolean overwrite=false;
		String defaultDPIPrefix=null;
		try {
		optsHandler.parse(args);
		in_drawables_folder = optsHandler.getInputFolder();
		out_values_parent_folder = optsHandler.getOutputFolder();
		overwrite = optsHandler.getIfOverwrite();
		defaultDPIPrefix = optsHandler.getDefaultDPIPrefix();
		} catch(Exception e)
		{
			System.err.println("Exception while processing command line arguments.  Program will terminate with no modified files.");
			e.printStackTrace();
			System.exit(EXIT_FAILURE);
		}
		
		FileChecks.checkDrawableInputFolder(in_drawables_folder);
		
		//check if the output dir is empty, create it if it doesn't exist
		boolean empty = false;
		try {
			if(out_values_parent_folder.exists())
				empty = FileChecks.dirIsEmptyWarnHidden(out_values_parent_folder);
			else
			{
				if(!out_values_parent_folder.mkdir())
					throw new IOException("Failed to create dir: "+out_values_parent_folder.toString());
			}
		}
		catch(IOException e)
		{
			System.err.println("Fatal IO exception while checking if the output directory is empty.  Program will not exit.");
			e.printStackTrace();
			System.exit(EXIT_FAILURE);
		}
		
		//no point in deleting & remaking the output folder if it's already empty
		if(!empty && overwrite == true)
		{
			final File folderToDelete = out_values_parent_folder;
			System.out.println("--overwrite-on recognized.  Deleting output directory "+folderToDelete.toString());
			//overwite on
			//delete the output dir
			//outputDir.delete();
			try {
				//delete the folder if it exists
				if(folderToDelete.exists())
				{
					//use Apache IO Commons to delete the folder
					FileUtils.deleteDirectory(folderToDelete);
					
					//don't forget to re-create the directory after!
					if(!folderToDelete.mkdir())
						throw new IOException("Failed to recreate directory "+folderToDelete.toString()+" after deleting it!");
				}
			}
			catch(IOException e)
			{
				System.err.println("ERROR: Could not delete output directory!");
				System.exit(EXIT_FAILURE);
			}
		}

		
		//map the assets to output data
		Mapper mapper = new Mapper(in_drawables_folder, out_values_parent_folder);
		Map<String, ArrayList<Pair<String, String>>> pairMap = mapper.getValuesToPair();
		
		//transform the assets:output data map to XML and write it
		MapWriter writer = new MapWriter(out_values_parent_folder);
		writer.write(pairMap);
		
		//perform postprocessing on the XML files
		ArrayList<File> writtenXMLFiles = writer.getWrittenFiles();
		try {
		XMLPostProcessor.removeStandaloneAttributes(writtenXMLFiles);
		}
		catch(IOException e)
		{
			System.err.println("Fatal IO exception during XML postprocessing (remove standalone=no/yes attribute)");
			e.printStackTrace();
			System.exit(EXIT_FAILURE);
		}
		
		//handle the default DPI prefix option
		try
		{
			XMLPostProcessor.copyDefaultDPI(defaultDPIPrefix, out_values_parent_folder, pairMap);
		}
		catch(IOException e)
		{
			//probably will never get here--an IO error would happen earlier
			System.err.println("Error while copying the values-"+defaultDPIPrefix+" strings.xml to values folder.");
			e.printStackTrace();
			System.exit(EXIT_FAILURE);
		} 
	}
}
