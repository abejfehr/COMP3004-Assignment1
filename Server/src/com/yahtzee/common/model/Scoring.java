package com.yahtzee.common.model;

import java.util.Arrays;

public class Scoring {
	
	public static final int ONES = 0;
	public static final int TWOS = 1;
	public static final int THREES = 2;
	public static final int FOURS = 3;
	public static final int FIVES = 4;
	public static final int SIXES = 5;
	public static final int THREE_OF_A_KIND = 6;
	public static final int FOUR_OF_A_KIND = 7;
	public static final int FULL_HOUSE = 8;
	public static final int SMALL_STRAIGHT = 9;
	public static final int LARGE_STRAIGHT = 10;
	public static final int CHANCE = 11;
	public static final int YAHTZEE = 12;
	
	
	
	// We'll keep the scores in here as well
	public int[] playerScores = new int[13];
	public int[] totalScores = new int[13];
	
	
	
	/*
	 *  Need to have a place to calculate the score
	 */
	public static final int getScoreFor(int category, int[] dice) {
		// Base case, if any of the dice are 0, then don't even bother
		if(dice[0] == 0 || dice[1] == 0 || dice[2] == 0 || dice[3] == 0 || dice[4] == 0)
			return 0;
		
		int score = 0;
		
		// Before we go into the categories, let's find the largest number of the same dice
		int max = 0;
		int val = 0; // ...and the value of those dice
		for(int i=0;i<5;++i) {
			int num = 0;
			for(int j=0;j<5;++j) {
				if(dice[i] == dice[j])
					++num;
			}
			if(max < num) {
				max = num;
				val = dice[i];
			}
		}
		
		// Check for straights
		int[] sortedDice = new int[5];
		for(int i=0;i<5;++i) {
			sortedDice[i] = dice[i];
		}
		System.out.println(sortedDice);
		int maxStraightLength  = 1;
		int straightLength = 1;
		Arrays.sort(sortedDice);
		for(int i=1;i<5;++i) {
			if(sortedDice[i-1] == sortedDice[i] - 1)
				straightLength += 1;
			else {
				if(straightLength > maxStraightLength) {
					maxStraightLength = straightLength;
				}
				straightLength = 1;				
			}
		}
		
		switch(category) {
		case ONES:
			for(int i=0;i<5;++i) {
				if(dice[i] == 1)
					score += dice[i];
			}
			break;
		case TWOS:
			for(int i=0;i<5;++i) {
				if(dice[i] == 2)
					score += dice[i];
			}
			break;
		case THREES:
			for(int i=0;i<5;++i) {
				if(dice[i] == 3)
					score += dice[i];
			}
			break;
		case FOURS:
			for(int i=0;i<5;++i) {
				if(dice[i] == 4)
					score += dice[i];
			}
			break;
		case FIVES:
			for(int i=0;i<5;++i) {
				if(dice[i] == 5)
					score += dice[i];
			}
			break;
		case SIXES:
			for(int i=0;i<5;++i) {
				if(dice[i] == 6)
					score += dice[i];
			}
			break;
		case THREE_OF_A_KIND:
			if(max == 3) {
				for(int i=0;i<5;++i) {
					score += dice[i];
				}
			}				
			break;
		case FOUR_OF_A_KIND:
			if(max == 4) {
				for(int i=0;i<5;++i) {
					score += dice[i];
				}
			}				
			break;
		case FULL_HOUSE:
			int pairvalue = -1;
			if(max == 3) {
				for(int i=0;i<5;++i) {
					if(dice[i] != val)
						pairvalue = val;
					else if(dice[i] != val && pairvalue == dice[i]) {
						score = 25;
					}
				}
			}
			break;
		case SMALL_STRAIGHT:
			if(maxStraightLength == 4)
				score = 30;
			break;
		case LARGE_STRAIGHT:
			if(maxStraightLength == 5)
				score = 40;
			break;
		case YAHTZEE:
			if(max == 5) {
				for(int i=0;i<5;++i) {
					score = 50;
				}
			}
			break;
		case CHANCE:
			for(int i=0;i<5;++i) {
				score += dice[i];
			}
			break;
		}
		
		return score;
	}

}
