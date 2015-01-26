package com.yahtzee.common.model;

import java.util.Random;

import javax.swing.JButton;

/*
 *  Player.java
 *  
 *  The player represents you, the player of the actual game. Various pieces of
 *  information(stats) about the player are stored while they play.
 *  
 *  
 *  Attributes
 *  ==========
 *  username - The username of the player in question. It should ideally be
 *  unique, but that won't be enforced.
 *  
 *  dice - An array of integers which store the values of all the player's
 *  dice.
 *  
 *  held - An integer which is actually a bitstring of length 5(not enforced)
 *  that represents which dice are held. For example, 00101 indicates that 
 *  the dice at indices 2 and 4 are held, but the others are free to roll.
 *  
 *  score - An integer which will store the user's running total score.
 *  
 *  rollsRemaining - The number of rolls which the user has left for that
 *  round. The default number of rolls per round is 3.
 *  
 *  
 *  Methods
 *  =======
 *  toggleHold(n) - Tells the player to hold a particular die, or to un-hold it
 *  if it's held, where n is the index of that die.
 *  
 *  roll() - Rolls the player's unheld dice.
 *  
 *  isHeld(n) - A helper function that returns whether or not the nth dice is
 *  held.
 */

public class Player {

	// Main attributes for this class
	private String username;
	private int[] dice = new int[5];
	private int held = 0;
	private int score = 0;
	private int rollsRemaining = 3;
	public boolean roundFinished = false;
	
	// For functions to use
	private Random random = new Random();

	/*
	 *  Constructors
	 */
	public Player() {
		int  n = random.nextInt(500) + 1;
		this.username = "Player" + n;
	}

	public Player(String username) {
		this.username = username;
	}

	
	
	
	/*
	 *  Overridden functions
	 */
	@Override
	public String toString() {
		return username;
	}
	

	
	/*
	 *  Class functions
	 */
	public void toggleHold(int n) {
		held = held ^ (1 << (5 - n));
	}
	
	public void roll() {
		System.out.println(username + " is rolling the dice.");
		
		for(int i = 0; i < dice.length; ++i) {
			if(!isHeld(i))
				dice[i] = random.nextInt(6) + 1;
		}
		
		--rollsRemaining;
	}
	
	public boolean isHeld(int n) {
		return ((held >> (5 - n) & 1) == 1);
	}
	
	
	
	/*
	 *  Getters and setters
	 */
	public int[] getDice() {
		return dice;
	}
	
	public String getUsername() {
		return username;
	}

	public int getDieValue(int i) {
		return dice[i];
	}

	public boolean hasRolls() {
		return rollsRemaining > 0 && !roundFinished;
	}

	public void resetRollsRemaining() {
		rollsRemaining = 3;
	}

	public void resetHeld() {
		held = 0;
	}


}