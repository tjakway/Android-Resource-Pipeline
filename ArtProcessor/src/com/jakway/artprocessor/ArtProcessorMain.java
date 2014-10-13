package com.jakway.artprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FileUtils;

import com.jakway.artprocessor.errorhandler.TerminatingArtProcessorErrorHandler;
import com.jakway.artprocessor.errorhandler.TranscoderErrorHandler;
import com.jakway.artprocessor.file.FileSystemChecker;
import com.jakway.artprocessor.transcoder.ArtProcessorTranscoder;

public class ArtProcessorMain
{	
	private static final String USAGE="Arguments:\n1. The input directory containing only SVG images and no subdirectories.\n2. The output directory to write bitmaps to." +
			"optional arg: --overwrite=on to delete the output directory before writing." +
			"WARNING: the passed output directory will be deleted if any errors are detected during export";
	
	private static final int MIN_ARGS=2, MAX_ARGS=3;
	private static final String OVERWRITE_OPTION="--overwrite=on";
	private static final int EXIT_FAILURE=1;
	
	public static void main(String args[]) throws TranscoderException, FileNotFoundException
	{
		File inputDir = null,
				outputDir = null;
		
		if(args.length < MIN_ARGS || args.length > MAX_ARGS)
		{
			System.out.println(USAGE);
			System.exit(EXIT_FAILURE);
		}
		
		inputDir = new File(args[0]);
		outputDir = new File(args[1]);
		
		//optional argument was passed
		if(args.length == 3)
		{
			String optionalArg = args[2];
			if(!optionalArg.equals(OVERWRITE_OPTION))
			{
				System.out.println("Didn't recognize 3rd optional argument.");
				System.out.println(USAGE);
				System.exit(EXIT_FAILURE);
			}
			else
			{
				System.out.println("--overwrite-on recognized.  Deleting output directory "+outputDir.toString());
				//overwite on
				//delete the output dir
				//outputDir.delete();
				try {
				FileUtils.deleteDirectory(outputDir);
				}
				catch(IOException e)
				{
					System.err.println("ERROR: Could not delete output directory!");
					System.exit(EXIT_FAILURE);
				}
			}
		}
		
		if(!inputDir.exists())
		{
			System.err.println("input dir "+inputDir+"doesnt exist!");
			System.exit(1);
		}
		
		FileSystemChecker checker = new FileSystemChecker(inputDir, new TerminatingArtProcessorErrorHandler("FileSystemChecker"));
		
		ArrayList<File> svgFiles = checker.checkFiles();
		
		//VALIDATOR NOT USED
		//SVGValidator validator = new SVGValidator(svgFiles, new TerminatingArtProcessorErrorHandler("SVGValidator"));
		//validator.validateSVGs();
		
		ArtProcessorTranscoder transcoder = new ArtProcessorTranscoder(outputDir, svgFiles, new TranscoderErrorHandler("ArtProcessorTranscoder", outputDir));
		transcoder.convertAndWriteSVGs();
	}
}
