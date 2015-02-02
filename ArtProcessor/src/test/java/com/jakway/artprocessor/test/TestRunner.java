package com.jakway.artprocessor.test;

import org.junit.runner.notification.Failure;
import org.junit.runner.Result;
import org.junit.runner.JUnitCore;

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
