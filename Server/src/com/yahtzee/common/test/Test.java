package com.yahtzee.common.test;

public class Test {

	public void compare(String testname, String expected, String actual) {
		System.out.println("Testing " + testname);
		System.out.println("====================");
		System.out.println("Expected value: " + expected);
		System.out.println("Actual value: " + actual);
		System.out.println("The test " +
				(expected.equals(actual) ? "passed." : "failed."));
		System.out.println("");
	}

}
