package com.jakway.artprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FileUtils;

import com.beust.jcommander.JCommander;
import com.jakway.artprocessor.errorhandler.TerminatingArtProcessorErrorHandler;
import com.jakway.artprocessor.errorhandler.TranscoderErrorHandler;
import com.jakway.artprocessor.file.FileSystemChecker;
import com.jakway.artprocessor.transcoder.ArtProcessorTranscoder;

public class ArtProcessorMain
{	
	public static void main(String args[]) throws TranscoderException, FileNotFoundException
	{
		Options options = new Options();	
	
		new JCommander(options, args);

		if(options.getOverwrite())
		{
			System.out.println("overwrite option recognized.  Deleting output directory "+options.getOutputDir());
			try {
			FileUtils.deleteDirectory(options.getOutputDir());
			}
			catch(IOException e)
			{
				System.err.println("ERROR: Could not delete output directory!");
				System.exit(1);
			}
		}

		if(!options.getInputDir().exists())
		{
			System.err.println("input dir "+options.getInputDir()+"doesnt exist!");
			System.exit(1);
		}

		//output dir doesn't exist--try to create it
		if(!options.getOutputDir().exists())
		{
			if(!options.getOutputDir().mkdir())
			{
				System.err.println("output dir "+options.getOutputDir()+" does not exist and could not be created!");	
				System.exit(1);
			}
		}

		FileSystemChecker checker = new FileSystemChecker(options.getInputDir(), new TerminatingArtProcessorErrorHandler("FileSystemChecker"));
		
		ArrayList<File> svgFiles = checker.checkFiles();
		
		//VALIDATOR NOT USED
		//SVGValidator validator = new SVGValidator(svgFiles, new TerminatingArtProcessorErrorHandler("SVGValidator"));
		//validator.validateSVGs();
		
		ArtProcessorTranscoder transcoder = new ArtProcessorTranscoder(options.getOutputDir(), svgFiles, new TranscoderErrorHandler("ArtProcessorTranscoder", options.getOutputDir()));
		transcoder.convertAndWriteSVGs();
	}
}
