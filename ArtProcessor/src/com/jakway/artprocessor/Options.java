package com.jakway.artprocessor;
import java.io.File;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.Parameter;

public class Options
{
	@Parameter(names = { "in", "input", "input-folder" }, description="Input folder containing source images", converter = FileConverter.class)
	private File input;

	
	@Parameter(names = { "out", "output", "output-folder" }, description="Input folder containing source images", converter = FileConverter.class)
	private File output;

	@Parameter(names="overwrite", required = false, description="Delete the output folder before writing.  WARNING: the passed output directory will be deleted if any errors are detected during export")
	private boolean overwrite;

	/**
	 * @return the input
	 */
	public File getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(File input) {
		this.input = input;
	}

	/**
	 * @return the output
	 */
	public File getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(File output) {
		this.output = output;
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
