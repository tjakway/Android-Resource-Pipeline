package com.jakway.artprocessor.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

public class ArtProcessorMain implements org.apache.batik.transcoder.ErrorHandler
{
	private static Float jpegQuality = new Float(1.0);
	private static FileInputStream inStream = null;
	private static FileOutputStream outStream = null;
	
	public static void main(String args[]) throws TranscoderException, FileNotFoundException
	{
		try{
        JPEGTranscoder transcoder = new JPEGTranscoder();
        
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, jpegQuality);
        
        inStream = new FileInputStream(new File("Example.svg"));
        
        TranscoderInput transcoderInput = new TranscoderInput(inStream);
        
        outStream = new FileOutputStream(new File("Output_example.jpeg"));
        
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
