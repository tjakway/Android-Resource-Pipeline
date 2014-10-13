package com.jakway.artprocessor.transcoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import com.jakway.artprocessor.errorhandler.ArtProcessorErrorHandler;
import com.jakway.artprocessor.exception.ArtProcessorException;
import com.jakway.artprocessor.file.FileSystemChecker;

public class ArtProcessorTranscoder
{
	private static final JPEGRasterOutputParams[] params = {
		new JPEGRasterOutputParams("drawable-ldpi", new Float(0.75f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-mdpi", new Float(1.0f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-hdpi", new Float(1.5f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-xhdpi", new Float(2.0f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-xxhdpi", new Float(3.0f), new Float(1.0f)),
		//don't use drawable-xxxhdpi--it's for app icons only
	};
	
	private ArtProcessorErrorHandler errorHandler = null;
	private File topLevelOutputDirectory = null;
	private ArrayList<File> svgFiles = null;
	
	public ArtProcessorTranscoder(File topLevelOutputDirectory, ArrayList<File> svgFiles, ArtProcessorErrorHandler errorHandler)
	{
		this.errorHandler = errorHandler;
		this.topLevelOutputDirectory = topLevelOutputDirectory;
		this.svgFiles = svgFiles;
	}

	public void convertAndWriteSVGs()
	{
		if(topLevelOutputDirectory == null)
		{
			errorHandler.fatalError(new ArtProcessorException("Passed top level art output directory is null."));
		}
		
		if(!topLevelOutputDirectory.exists())
		{
			errorHandler.fatalError(new ArtProcessorException("Top level art output directory does not exist."));
		}
		
		//best way to check if we can write to the output directory is to try and write something
		//see http://stackoverflow.com/questions/1272130/checking-for-write-access-in-a-directory-before-creating-files-inside-it
		//canWrite is not sufficient because it checks that the file exists
		try{
			File test = new File(topLevelOutputDirectory, "test_temp_file");
			test.createNewFile();
			test.delete();
		}
		catch(IOException e)
		{
			errorHandler.fatalError(new ArtProcessorException("Cannot write to the top level art output directory."));
		}
		
		//write by directory, first drawables-ldpi, then drawables-mdpi, etc.
		for(int i = 0; i < params.length; i++)
		{
			JPEGRasterOutputParams thisParam = params[i];
			
			File outputDir = new File(topLevelOutputDirectory, thisParam.getOutputDirName());
			
			//write every SVG to every drawables directory
			for(File thisSVGFile : svgFiles)
			{
				try {
				convertAndWriteSVG(thisSVGFile, outputDir, thisParam);
				}
				catch(FileNotFoundException e)
				{
					errorHandler.error(new ArtProcessorException("Could not find input SVG file: "+thisSVGFile.toString()));
				}
				catch(TranscoderException e)
				{
					errorHandler.error(new ArtProcessorException("Transcoder problem while converting svg file: "+thisSVGFile.toString()+" in directory: "+outputDir.toString()));
				}
			}
		}
	}
	
	private void convertAndWriteSVG(File inputSVG, File outputDir,  JPEGRasterOutputParams param) throws FileNotFoundException, TranscoderException
	{
		//generate the output filename and make sure it doesn't already exist
		
		//strip the extension from the input and add .svg
		File outputFile = new File(outputDir, getFilenameNoExtension(inputSVG)+FileSystemChecker.SVG_EXTENSION);
		
		if(outputFile.exists())
		{
			errorHandler.fatalError(new ArtProcessorException(outputFile.toString()+" already exists in the art output directory."));
		}
		
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		//write the SVG
		try{
	        JPEGTranscoder transcoder = new JPEGTranscoder();
	        
	        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, param.getJpegQuality());
	        
	        inStream = new FileInputStream(inputSVG);
	        
	        TranscoderInput transcoderInput = new TranscoderInput(inStream);
	        
	        outStream = new FileOutputStream(outputFile);
	        
	        TranscoderOutput transcoderOutput = new TranscoderOutput(outStream);
	        transcoder.transcode(transcoderInput, transcoderOutput);
	        
			}
			finally
			{
				if(inStream != null)
				{
					try {inStream.close(); } catch(Throwable t) { }
				}
				if(outStream != null)
				{
					try {outStream.close(); } catch(Throwable t) { }
				}
			}
	}
	
	/**
	 * regex solution
	 * see http://stackoverflow.com/questions/924394/how-to-get-file-name-without-the-extension
	 * @param file
	 * @return
	 */
	private static final String getFilenameNoExtension(File file)
	{
		return file.toString().replaceFirst("[.][^.]+$", "");
	}
}
