package com.jakway.artprocessor.transcoder;


/**
 * container struct for all the parameters to rasterize an SVG file to a JPEG
 * @author thomasjakway
 *
 */
public class JPEGRasterOutputParams
{
	private String outputDirName=null;
	private Float sizeMultiplier=null;
	private Float jpegQuality=null;
	public JPEGRasterOutputParams(String outputDirName, Float sizeMultiplier,
			Float jpegQuality)
	{
		this.outputDirName = outputDirName;
		this.sizeMultiplier = sizeMultiplier;
		this.jpegQuality = jpegQuality;
	}
	public String getOutputDirName()
	{
		return outputDirName;
	}
	public Float getSizeMultiplier()
	{
		return sizeMultiplier;
	}
	public Float getJpegQuality()
	{
		return jpegQuality;
	}
	
}