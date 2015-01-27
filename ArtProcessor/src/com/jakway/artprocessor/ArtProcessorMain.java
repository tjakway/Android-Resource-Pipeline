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
			System.out.println("overwrite option recognized.  Deleting output directory "+options.getOutput());
			try {
			FileUtils.deleteDirectory(options.getOutput());
			}
			catch(IOException e)
			{
				System.err.println("ERROR: Could not delete output directory!");
				System.exit(1);
			}
		}

		if(!options.getInput().exists())
		{
			System.err.println("input dir "+options.getInput()+"doesnt exist!");
			System.exit(1);
		}

		//output dir doesn't exist--try to create it
		if(!options.getOutput().exists())
		{
			if(!options.getOutput().mkdir())
			{
				System.err.println("output dir "+options.getOutput()+" does not exist and could not be created!");	
				System.exit(1);
			}
		}

		FileSystemChecker checker = new FileSystemChecker(options.getInput(), new TerminatingArtProcessorErrorHandler("FileSystemChecker"));
		
		ArrayList<File> svgFiles = checker.checkFiles();
		
		//VALIDATOR NOT USED
		//SVGValidator validator = new SVGValidator(svgFiles, new TerminatingArtProcessorErrorHandler("SVGValidator"));
		//validator.validateSVGs();
		
		ArtProcessorTranscoder transcoder = new ArtProcessorTranscoder(outputDir, svgFiles, new TranscoderErrorHandler("ArtProcessorTranscoder", outputDir));
		transcoder.convertAndWriteSVGs();
	}
}
