package com.jakway.stringsgen.options;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.jakway.stringsgen.prefixes.PrefixHandler;

public class OptionsHandler
{
	private static final String in="in", out="out", overwrite="overwrite", default_dpi="default_dpi", help_short="h", help_long="help";
	
	/**
	 * use cmd to access the parsed options
	 */
	private CommandLine cmd = null;
	/**
	 * use this to set up the parser
	 */
	private Options allOptions = new Options();
	
	public OptionsHandler()
	{
		//create the arguments array to use in parse()
		
		Option options[] = {
		//input folder option
		new Option(in, "input", true, "The input folder--should be the 'assets' folder."),
		//output folder option
		new Option(out, "output", true, "The output folder.  values-xxxx (e.g. values-ldpi, values-mdpi...) subfolders will be created in this folder."),
		//overwrite option (not required)
		new Option(overwrite, overwrite, true, "Optional --overwrite=on option to delete the output folder if it already exists before writing to it."),
		//default DPI option
		new Option(default_dpi, default_dpi, true, "The DPI prefix to use for the unqualified values folder that Android accesses by default.  NOT the folder name itself.  Pass, for example, hdpi to copy the values-hdpi/strings.xml to values/strings.xml"),
		new Option(help_short, help_long, false, "Print this help message.")
		};
		
		options[0].setRequired(true); //input folder
		options[1].setRequired(true); //output folder
		options[2].setRequired(false); //overwrite option
		options[3].setRequired(true); //default dpi
		options[4].setRequired(false);
		
		//setOptionalArg sets whether an option's argument can be omitted (false = cannot omit)
		options[0].setArgs(1); //input folder
		options[1].setArgs(1); //output folder
		options[2].setArgs(1); //overwrite option
		options[3].setArgs(1); //default dpi
		options[4].setArgs(0); //help option
		
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
		CommandLineParser parser = new DefaultParser();
		
		cmd = parser.parse(allOptions, args);
		
		//check for the help message
		if(cmd.hasOption(help_short))
		{
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Main", allOptions);
			
			System.exit(0); //exit success
		}
	}
	
	public File getInputFolder()
	{
		return new File(cmd.getOptionValue(in));
	}
	
	public File getOutputFolder()
	{
		return new File(cmd.getOptionValue(out));
	}
	
	/**
	 * @throws UnrecognizedOptionException if the value passed to --overwrite is not "on" or "off"
	 * @return true if the user passed --overwrite on, false if the user passed --overwrite off
	 */
	public boolean getIfOverwrite() throws UnrecognizedOptionException
	{
		//if it doesnt have the option, we obviously aren't overwriting
		if(!cmd.hasOption(overwrite))
			return false;
		
		//if it does have the option, check what it's set to
		String value = cmd.getOptionValue(overwrite);
		
		if(value.equals("on"))
			return true;
		else if(value.equals("off"))
			return false;
		else
		{
			throw new UnrecognizedOptionException("Did not recognize value passed with --"+overwrite+".  Acceptable values: on, off (--overwrite on, --overwrite off)", overwrite);
		}
	}
	
	/**
	 * returns the DPI prefix the user selected to use for the default values folder
	 * @return
	 * @throws UnrecognizedOptionException if the user passed an invalid prefix
	 */
	public String getDefaultDPIPrefix() throws UnrecognizedOptionException
	{
		String value = cmd.getOptionValue(default_dpi);
		
		//check if the passed value is a valid prefix
		for(String prefix : PrefixHandler.prefixes)
		{
			if(value.equals(prefix))
				return value;
		}
		//user passed an invalid prefix
		throw new UnrecognizedOptionException("Did not recognize value passed with --"+default_dpi+".  Acceptable values include ldpi, mdpi, hdpi, etc.", default_dpi);
	}
}
