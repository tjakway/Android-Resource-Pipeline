package com.jakway.artprocessor.transcoder;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import com.jakway.artprocessor.errorhandler.ArtProcessorErrorHandler;
import com.jakway.artprocessor.errorhandler.TranscoderErrorHandler;
import com.jakway.artprocessor.exception.ArtProcessorException;
import com.jakway.artprocessor.file.FileSystemChecker;
import com.jakway.artprocessor.svg.SVGValidator;

public class ArtProcessorTranscoder
{
	private static final String JPEG_EXTENSION=".jpeg";
	
	private static final JPEGRasterOutputParams[] params = {
		new JPEGRasterOutputParams("drawable-ldpi", new Float(0.75f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-mdpi", new Float(1.0f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-hdpi", new Float(1.5f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-xhdpi", new Float(2.0f), new Float(1.0f)),
		new JPEGRasterOutputParams("drawable-xxhdpi", new Float(3.0f), new Float(1.0f)),
		//don't use drawable-xxxhdpi--it's for app icons only
	};
	
	private TranscoderErrorHandler errorHandler = null;
	private File topLevelOutputDirectory = null;
	private ArrayList<File> svgFiles = null;
	
	public ArtProcessorTranscoder(File topLevelOutputDirectory, ArrayList<File> svgFiles, TranscoderErrorHandler errorHandler)
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
			topLevelOutputDirectory.mkdir();
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
			
			outputDir.mkdir();
			
			//write every SVG to every drawables directory
			for(File thisSVGFile : svgFiles)
			{
				try {
				convertAndWriteSVG(thisSVGFile, outputDir, thisParam);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
					errorHandler.error(new ArtProcessorException("Could not find input SVG file: "+thisSVGFile.toString()));
				}
				catch(IOException e)
				{
					e.printStackTrace();
					errorHandler.error(new ArtProcessorException("IO Error while writing SVG file: "+thisSVGFile.toString()));
				}
				catch(TranscoderException e)
				{
					e.printStackTrace();
					errorHandler.error(new ArtProcessorException("Transcoder problem while converting svg file: "+thisSVGFile.toString()+" in directory: "+outputDir.toString()));
				}
			}
		}
	}
	
	/**
	 * see http://batik.2283329.n4.nabble.com/General-difficulties-and-problems-getting-the-size-right-in-painting-an-SVG-document-containing-a-qu-td4100365.html
	 */
	private Rectangle2D getSVGDimensions(File svgFile) throws IOException
	{
		org.apache.batik.gvt.GraphicsNode svgIcon = null; 
        try { 
            String xmlParser = org.apache.batik.util.XMLResourceDescriptor.getXMLParserClassName(); 
            org.apache.batik.dom.svg.SAXSVGDocumentFactory df = new org.apache.batik.dom.svg.SAXSVGDocumentFactory( 
                xmlParser); 
            org.w3c.dom.svg.SVGDocument doc = df.createSVGDocument(SVGValidator.convertToFileURL(svgFile)); 
            org.apache.batik.bridge.UserAgent userAgent = new org.apache.batik.bridge.UserAgentAdapter(); 
            org.apache.batik.bridge.DocumentLoader loader = new org.apache.batik.bridge.DocumentLoader(userAgent); 
            org.apache.batik.bridge.BridgeContext ctx = new org.apache.batik.bridge.BridgeContext(userAgent, loader); 
            ctx.setDynamicState(org.apache.batik.bridge.BridgeContext.DYNAMIC); 
            org.apache.batik.bridge.GVTBuilder builder = new org.apache.batik.bridge.GVTBuilder(); 
            svgIcon = builder.build(ctx, doc); 
            
            xmlParser=null;
            df=null;
            doc=null;
            userAgent=null;
            loader=null;
            ctx=null;
            builder=null;
        } catch (Exception excp) { 
            svgIcon = null; 
            excp.printStackTrace(); 
        }
        Rectangle2D rect = svgIcon.getPrimitiveBounds();
        svgIcon=null;
        
        System.gc();
        return rect;
	}
	
	private void convertAndWriteSVG(File inputSVG, File outputDir,  JPEGRasterOutputParams param) throws TranscoderException, IOException
	{
		//generate the output filename and make sure it doesn't already exist
		
		//strip the extension from the input and add .svg
		File outputFile = new File(outputDir, getFilenameNoExtension(inputSVG)+JPEG_EXTENSION);
		
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
	        
	        //get SVG dimensions to use in the size multiplier
	        Rectangle2D rect = getSVGDimensions(inputSVG);
	        
	        //set dimensions based on the size multiplier
	        transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(param.getSizeMultiplier().floatValue() * rect.getWidth()));
	        
	        transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(param.getSizeMultiplier().floatValue() * rect.getHeight()));
	        
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
		return file.getName().replaceFirst("[.][^.]+$", "");
	}
}
