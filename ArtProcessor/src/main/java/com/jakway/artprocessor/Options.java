package com.jakway.artprocessor;
import java.io.File;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.Parameter;

public class Options
{
	@Parameter(names = { "-in", "--input", "input-folder" }, required = true, description="Input folder containing source images", converter = FileConverter.class)
	private File inputDir;

	
	@Parameter(names = { "-out", "--output", "output-folder" }, required = true, description="Input folder containing source images", converter = FileConverter.class)
	private File outputDir;

	@Parameter(names="--overwrite", required = false, description="Delete the output folder before writing.  WARNING: the passed output directory will be deleted if any errors are detected during export")
	private boolean overwrite=false;

	/**
	 * @return the input
	 */
	public File getInputDir() {
		return inputDir;
	}

	/**
	 * @param input the input to set
	 */
	public void setInputDir(File input) {
		this.inputDir = input;
	}

	/**
	 * @return the output
	 */
	public File getOutputDir() {
		return outputDir;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * @return the overwrite
	 */
	public boolean getOverwrite() {
		return overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
}
