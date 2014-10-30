package com.jakway.stringsgen.options;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class OptionsHandler
{
	private Options allOptions = new Options();
	
	public OptionsHandler()
	{
		//create the arguments array to use in parse()
		
		Option options[] = {
		//input folder option
		new Option("in", "input", true, "The input folder--should be the 'assets' folder."),
		//output folder option
		new Option("out", "output", true, "The output folder.  values-xxxx (e.g. values-ldpi, values-mdpi...) subfolders will be created in this folder."),
		//overwrite option (not required)
		new Option("overwrite", true, "Optional --overwrite=on option to delete the output folder if it already exists before writing to it."),
		//default DPI option
		new Option("default-dpi", true, "The DPI prefix to use for the unqualified values folder that Android accesses by default.  NOT the folder name itself.  Pass, for example, hdpi to copy the values-hdpi/strings.xml to values/strings.xml")
		};
		
		options[0].setRequired(true); //input folder
		options[1].setRequired(true); //output folder
		options[2].setRequired(false); //overwrite option
		options[3].setRequired(true); //default dpi
		
		//setOptionalArg sets whether an option's argument can be omitted (false = cannot omit)
		options[0].setOptionalArg(false); //input folder
		options[1].setOptionalArg(false); //output folder
		options[2].setOptionalArg(false); //overwrite option
		options[3].setOptionalArg(false); //default dpi
		
		for(Option opt : options)
		{
			allOptions.addOption(opt);
		}
	}
	
	/**
	 * parse using GNU style arguments, e.g. --myoption value
	 * @param args
	 * @throws ParseException 
	 */
	public void parse(String[] args) throws ParseException
	{
		CommandLineParser parser = new GnuParser();
		
		parser.parse(allOptions, args);
	}
}
