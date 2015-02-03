package com.jakway.artprocessor.test.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;

public class TestUtils
{
	/**
	 * This method is silent if the path does not exist (it won't fail if it's already been deleted).
	 */
	public static final void assertDelete(Path path) throws IOException
	{
		//silently return if the path is null or has already been deleted
		if(path == null || Files.notExists(path))
			return;
		//use Apache IO Commons forceDelete over Files.delete
		//to delete subdirectories
		FileUtils.forceDelete(path.toFile());
		//make sure we were able to clean up properly
		assertTrue(Files.notExists(path));
	}

	public static final void assertDelete(File file) throws IOException
	{
		assertDelete(file.toPath());	
	}
}
