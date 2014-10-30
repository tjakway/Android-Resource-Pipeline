package com.jakway.stringsgen.options;

import java.io.File;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.jakway.stringsgen.file.FileChecks;

public class OptionsHandler
{
	private Options allOptions = new Options();
	private static final String in="in", out="out", overwrite="overwrite", default_dpi="default-dpi";
	
	public OptionsHandler()
	{
		//create the arguments array to use in parse()
		
		Option options[] = {
		//input folder option
		new Option(in, "input", true, "The input folder--should be the 'assets' folder."),
		//output folder option
		new Option(out, "output", true, "The output folder.  values-xxxx (e.g. values-ldpi, values-mdpi...) subfolders will be created in this folder."),
		//overwrite option (not required)
		new Option(overwrite, true, "Optional --overwrite=on option to delete the output folder if it already exists before writing to it."),
		//default DPI option
		new Option(default_dpi, true, "The DPI prefix to use for the unqualified values folder that Android accesses by default.  NOT the folder name itself.  Pass, for example, hdpi to copy the values-hdpi/strings.xml to values/strings.xml")
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
	
	public File getInputFolder()
	{
		return new File(allOptions.getOption(in).getValue());
	}
	
	public File getOutputFolder()
	{
		return new File(allOptions.getOption(out).getValue());
	}
	
	/**
	 * @throws UnrecognizedOptionException if the value passed to --overwrite is not "on" or "off"
	 * @return true if the user passed --overwrite on, false if the user passed --overwrite off
	 */
	public boolean getIfOverwrite() throws UnrecognizedOptionException
	{
		String value = allOptions.getOption(overwrite).getValue();
		
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
	public String getDefaultDPI() throws UnrecognizedOptionException
	{
		String value = allOptions.getOption(default_dpi).getValue();
		
		//check if the passed value is a valid prefix
		for(String prefix : FileChecks.prefixes)
		{
			if(value.equals(prefix))
				return value;
		}
		//user passed an invalid prefix
		throw new UnrecognizedOptionException("Did not recognize value passed with --"+default_dpi+".  Acceptable values include ldpi, mdpi, hdpi, etc.", default_dpi);
	}
}
