package com.jakway.stringsgen.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ArgsUtils
{
	private static final int EXIT_FAILURE=1;
	private static final String OVERWRITE_OPTION="--overwrite=on";
	
	/**
	 * checks if the 3rd parameter was --overwrite=on
	 * if it is, it deletes the passed folder
	 * if the a 3rd parameter was passed by does not match --overwrite=on, the usage string is printed and System.exit is called
	 * uses Apache IO Commons to delete folderToDelete
	 * @param usage
	 */
	public static final void checkOverwriteOption(String[] args, File folderToDelete, String usage)
	{
		//optional argument was passed
		if(args.length == 3)
		{
			String optionalArg = args[2];
			if(!optionalArg.equals(OVERWRITE_OPTION))
			{
				System.out.println("Didn't recognize 3rd optional argument.");
				System.out.println(usage);
				System.exit(EXIT_FAILURE);
			}
			else
			{
				System.out.println("--overwrite-on recognized.  Deleting output directory "+folderToDelete.toString());
				//overwite on
				//delete the output dir
				//outputDir.delete();
				try {
					//delete the folder if it exists
					if(folderToDelete.exists())
					{
						//use Apache IO Commons to delete the folder
						FileUtils.deleteDirectory(folderToDelete);
						
						//don't forget to re-create the directory after!
						if(!folderToDelete.mkdir())
							throw new IOException("Failed to recreate directory "+folderToDelete.toString()+" after deleting it!");
					}
				}
				catch(IOException e)
				{
					System.err.println("ERROR: Could not delete output directory!");
					System.exit(EXIT_FAILURE);
				}
			}
		}
	}

}
