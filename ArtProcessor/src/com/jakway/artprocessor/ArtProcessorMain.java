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
import com.jakway.artprocessor.exception.ArtProcessorException;
import com.jakway.artprocessor.file.FileSystemChecker;

public class ArtProcessorMain implements org.apache.batik.transcoder.ErrorHandler
{	
	private static final String USAGE="Arguments:\n1. The input directory containing only SVG images and no subdirectories.\n2. The output directory to write bitmaps to." +
			"WARNING: the passed output directory will be deleted if any errors are detected during export";
	
	public static void main(String args[]) throws TranscoderException, FileNotFoundException
	{
		File inputDir = null;
		
		
		FileSystemChecker checker = new FileSystemChecker(inputDir, new TerminatingArtProcessorErrorHandler("FileSystemChecker"));
		
		ArrayList<File> svgFiles = checker.checkFiles();
		

		
		
	}
	
	@Override
	public void error(TranscoderException e) throws TranscoderException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatalError(TranscoderException e) throws TranscoderException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warning(TranscoderException e) throws TranscoderException
	{
		// TODO Auto-generated method stub
		
	}
}
