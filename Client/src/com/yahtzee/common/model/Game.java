package com.yahtzee.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/*
 *  Game.java
 *  
 *  This class stores each individual match for this game.
 *  
 *  
 *  Attributes
 *  ==========
 *  title - Each game has a name, and this is where it's stored.
 *  
 *  players - A list of players that are in the game
 *  
 *  messages - A string that contains the entire message history for that game.
 *  This can be found in the waiting area of the chat window while waiting for
 *  the game to begin.
 *  
 *  ready - The set of players that are ready to begin the game.  
 *  
 *  
 *  Methods
 *  =======
 *  
 */

public class Game {
	
	private String title = "";
	private ArrayList<Player> players = new ArrayList<Player>();
	private String messageText = "";
	private HashSet<Player> ready = new HashSet<Player>();
	
	public HashMap<Integer,Integer> playerScores = new HashMap<Integer,Integer>();
	public HashMap<Integer,Integer> totalScores = new HashMap<Integer,Integer>();

	
	/*
	 *  Constructors
	 */
	public Game() {
		this.title = "Default Title";
	}
	
	public Game(String title) {
		this.title = title;
	}
	
	
	
	/*
	 *  Overridden functions
	 */
	@Override
	public String toString() {
		return title;
	}
	
	
	
	/*
	 *  Getters and setters
	 */
	public String getTitle() {
		return title;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player p) {
		players.add(p);		
	}
	
	public String getMessageText() {
		return messageText;
	}
	
	public HashSet<Player> getReadyPlayers() {
		return ready;
	}

	public void setTitle(String gameTitle) {
		title = gameTitle;		
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;		
	}

	
	
	/*
	 *  Adds the player to the ready list and returns whether or not the game
	 *  should start.
	 */
	public boolean setReady(Player p) {
		ready.add(p);
		return (ready.size() == players.size());
	}

	public void addMessage(String message) {
		messageText += "\n" + message;
	}

	public boolean isReady() {
		return (ready.size() == players.size());
	}
	
	public void removePlayer(Player key) {
		players.remove(key);
	}
	
	public void resetScores() {
		playerScores = new HashMap<Integer, Integer>();
		totalScores = new HashMap<Integer, Integer>();
	}
}