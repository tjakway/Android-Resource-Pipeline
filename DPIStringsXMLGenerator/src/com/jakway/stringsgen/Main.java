package com.jakway.stringsgen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.jakway.stringsgen.file.ArgsUtils;
import com.jakway.stringsgen.file.FileChecks;
import com.jakway.stringsgen.map.StringsGenMapper;
import com.jakway.stringsgen.misc.Pair;

public class Main
{
	private static final String USAGE="USAGE_STR";
	private static final int MIN_ARGS=2, MAX_ARGS=3;
	private static final int EXIT_FAILURE=1;
	
	public static void main(String[] args)
	{
		//FIXME: DEBUG
		args = new String[] {"assets", "values_out", "--overwrite=on" };
		//FIXME: END DEBUG
		
		//file and input checks
		if(args.length > MAX_ARGS || args.length < MIN_ARGS)
		{
			System.err.println(USAGE);
			System.exit(EXIT_FAILURE);
		}
		
		File in_drawables_folder = new File(args[0]),
				out_values_folder = new File(args[1]);
		
		FileChecks.checkDrawableInputFolder(in_drawables_folder);
		
		boolean empty = false;
		try {
			if(out_values_folder.exists())
				empty = FileChecks.dirIsEmptyWarnHidden(out_values_folder);
			else
			{
				if(!out_values_folder.mkdir())
					throw new IOException("Failed to create dir: "+out_values_folder.toString());
			}
		}
		catch(IOException e)
		{
			System.err.println("Fatal IO exception while checking if the output directory is empty.  Program will not exit.");
			e.printStackTrace();
			System.exit(EXIT_FAILURE);
		}
		
		if(!empty)
			ArgsUtils.checkOverwriteOption(args, out_values_folder, USAGE);
		
		StringsGenMapper mapper = new StringsGenMapper(in_drawables_folder, out_values_folder);
		Map<String, ArrayList<Pair<String, String>>> pairMap = mapper.getValuesToPair();
		
		for(Map.Entry<String, ArrayList<Pair<String, String>>> entry : pairMap.entrySet())
		{
			System.out.println("Map key: "+entry.getKey());
			
			ArrayList<Pair<String, String>> list = entry.getValue();
			for(Pair<String, String> pair : list)
			{
				System.out.println("pair left: "+pair.getLeft()+", pair right: "+pair.getRight());
			}
		}
		
		System.out.println();
	}
}
