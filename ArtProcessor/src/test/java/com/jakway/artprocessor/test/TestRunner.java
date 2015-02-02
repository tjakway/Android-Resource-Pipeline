package com.jakway.artprocessor.test;

public class TestRunner
{
    public void main(String[] args)
    {
        Result result = JUnitCore.runClasses(BlackBoxTests.class); 
        for (Failure failure : result.getFailures())
        {
            System.err.println(failure.toString()); 
        }
    } 
}
