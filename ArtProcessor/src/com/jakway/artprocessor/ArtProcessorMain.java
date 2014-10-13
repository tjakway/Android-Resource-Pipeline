package com.jakway.artprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import com.jakway.artprocessor.errorhandler.ArtProcessorErrorHandler;
import com.jakway.artprocessor.errorhandler.TerminatingArtProcessorErrorHandler;
import com.jakway.artprocessor.errorhandler.TranscoderErrorHandler;
import com.jakway.artprocessor.exception.ArtProcessorException;
import com.jakway.artprocessor.file.FileSystemChecker;
import com.jakway.artprocessor.svg.SVGValidator;
import com.jakway.artprocessor.transcoder.ArtProcessorTranscoder;

public class ArtProcessorMain
{	
	private static final String USAGE="Arguments:\n1. The input directory containing only SVG images and no subdirectories.\n2. The output directory to write bitmaps to." +
			"WARNING: the passed output directory will be deleted if any errors are detected during export";
	
	public static void main(String args[]) throws TranscoderException, FileNotFoundException
	{
		File inputDir = null,
				outputDir = null;
		
		System.err.println("WARNING: SVG VALIDATION DISABLED");
		//TODO: change to command line arguments
		inputDir = new File("in");
		if(!inputDir.exists())
		{
			System.err.println("input dir doesnt exist!");
			System.exit(1);
		}
		outputDir = new File("out");
		
		FileSystemChecker checker = new FileSystemChecker(inputDir, new TerminatingArtProcessorErrorHandler("FileSystemChecker"));
		
		ArrayList<File> svgFiles = checker.checkFiles();
		
		//SVGValidator validator = new SVGValidator(svgFiles, new TerminatingArtProcessorErrorHandler("SVGValidator"));
		//validator.validateSVGs();
		
		ArtProcessorTranscoder transcoder = new ArtProcessorTranscoder(outputDir, svgFiles, new TranscoderErrorHandler("ArtProcessorTranscoder", outputDir));
		transcoder.convertAndWriteSVGs();
	}
}
