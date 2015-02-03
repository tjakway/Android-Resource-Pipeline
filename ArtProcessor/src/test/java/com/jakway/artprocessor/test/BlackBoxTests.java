package com.jakway.artprocessor.test;

import org.junit.Test;
import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import com.jakway.artprocessor.test.util.TestUtils;
import static org.junit.Assert.*;

import java.nio.file.Paths;

public class BlackBoxTests
{
    public BlackBoxTests()
    { }

    /**
     * WARNING: uses Path instead of File and hence requires Java 7
     * see {@link http://docs.oracle.com/javase/tutorial/essential/io/legacy.html }
     */
    @Test
    public void testOverwriteOption() throws IOException
    {
	Path input = null,
	     tmpTextFile = null,
	     tmpBinFile = null;
	BufferedWriter writer=null;
	OutputStream os = null;
	try {
        	input = Files.createTempDirectory(".tmp");
		tmpTextFile = Paths.get(input.toAbsolutePath().toString(), "tmpTextFile");
		tmpBinFile = Paths.get(input.toAbsolutePath().toString(), "tmpBinFile");
	
		writer = Files.newBufferedWriter(tmpTextFile, Charset.forName("UTF-8")); 
		writer.write("TEST TEXT");

		os = new BufferedOutputStream(Files.newOutputStream(tmpBinFile));
		os.write((int)System.currentTimeMillis());
	}
	finally
	{
		TestUtils.assertDelete(input);
	}
    }    
}
