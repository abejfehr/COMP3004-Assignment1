package com.yahtzee.common.test;

import java.util.Arrays;

import com.yahtzee.common.model.Player;

/*
 *  TestPlayer.java
 *  
 *  This class is expected to hold all of the tests for the Player class.
 */

public class TestPlayer extends Test {
	
	public void runTests() {
		
		// Test 1: Player creation and default values
		Player p = new Player();
		compare("initial dice values", "[0, 0, 0, 0, 0]", Arrays.toString(p.getDice()));
		
		// Test 2: Rolling the dice
		p.roll();
		System.out.println("The dice were rolled. The value is " + Arrays.toString(p.getDice()));
		
		p.toggleHold(0);
		p.toggleHold(1);
		System.out.println("Holding the first and second die.");
		
		p.roll();
		System.out.println("The dice were rolled again. The new value is " + Arrays.toString(p.getDice()));
		
		p.toggleHold(0);
		p.toggleHold(4);
		System.out.println("The first die was un-held and the last one was held");
		
		p.roll();
		System.out.println("The dice were rolled again. The new value is " + Arrays.toString(p.getDice()));
	}
}