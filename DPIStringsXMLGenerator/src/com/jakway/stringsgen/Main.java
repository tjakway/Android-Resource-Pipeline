package com.jakway.stringsgen;

public class Main
{
	private static final String USAGE="";
	private static final int MIN_ARGS=2, MAX_ARGS=2;
	private static final int EXIT_FAILURE=1;
	
	public static void main(String[] args)
	{
		if(args.length > MAX_ARGS || args.length < MIN_ARGS)
		{
			System.err.println(USAGE);
			System.exit(EXIT_FAILURE);
		}
		
		
	}
}
