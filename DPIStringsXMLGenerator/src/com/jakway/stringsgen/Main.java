package com.jakway.stringsgen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.jakway.stringsgen.file.ArgsUtils;
import com.jakway.stringsgen.file.FileChecks;
import com.jakway.stringsgen.map.MapWriter;
import com.jakway.stringsgen.map.Mapper;
import com.jakway.stringsgen.misc.Pair;
import com.jakway.stringsgen.postprocessor.XMLPostProcessor;

public class Main
{
	private static final int MIN_ARGS=2, MAX_ARGS=3;
	private static final int EXIT_FAILURE=1;
	
	public static void main(String[] args)
	{	
		//file and input checks
		if(args.length > MAX_ARGS || args.length < MIN_ARGS)
		{
			System.err.println(USAGE);
			System.exit(EXIT_FAILURE);
		}
		
		File in_drawables_folder = new File(args[0]),
				out_values_parent_folder = new File(args[1]);
		
		FileChecks.checkDrawableInputFolder(in_drawables_folder);
		
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
		
		if(!empty)
			ArgsUtils.checkOverwriteOption(args, out_values_parent_folder, USAGE);
		
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
	}
}
