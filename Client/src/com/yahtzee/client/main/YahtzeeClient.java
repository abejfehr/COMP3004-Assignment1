package com.yahtzee.client.main;
import java.util.HashMap;

import com.yahtzee.common.model.Game;
import com.yahtzee.common.model.Player;

/*
 *  ClientModel.java
 *  
 *  This file will contain the *one* game which is currently being played on
 *  the client side as well as the list of games that the player has to choose
 *  from in the beginning.
 *  
 *  Attributes
 *  ==========
 *  currentGame - The current game that's ongoing on the client side.
 *  
 *  gamesList - A list of the games that are currently going on in the server.
 *  
 *  player - The player on the client, since there can only be one player at a time anyway.
 *  
 *  Methods
 *  =======
 */

public class YahtzeeClient {
	public Game currentGame;
	public HashMap<String, Integer> gamesList;
	public Player player;
	
	YahtzeeClient() {
		gamesList = new HashMap<String, Integer>();
	}
}
