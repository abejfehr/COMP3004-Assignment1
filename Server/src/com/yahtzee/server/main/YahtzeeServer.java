package com.yahtzee.server.main;

import java.util.ArrayList;

import com.yahtzee.common.model.Game;
import com.yahtzee.common.model.Player;

/*
 *  YahtzeeServer.java
 * 
 * 
 *  Attributes
 *  ==========
 *  gamesList - The list of all the games that are currently going on in the 
 *  server.
 * 
 * 
 *  Methods
 *  =======
 *  newGame(g) - Creates a new game on the server.
 * 
 */

public class YahtzeeServer {

	ArrayList<Game> gamesList;
	public ArrayList<Player> readyPlayers;
	
	YahtzeeServer() {
		gamesList = new ArrayList<Game>();
		readyPlayers = new ArrayList<Player>();
	}
	
	public void newGame(Game g) {
		gamesList.add(g);
	}
	
	/*
	 *  Gets the game from the list by title
	 */
	public Game getGame(String gameTitle) {
		for(int i=0;i<gamesList.size(); ++i) {
			if(gamesList.get(i).getTitle().equals(gameTitle)) {
				return gamesList.get(i);
			}
		}
		return null;
	}

	public void clearPlayer(Player key) {
		// Loop through the games and remove that player where present
		for(int i=0;i<gamesList.size();++i) {
			gamesList.get(i).removePlayer(key);
			if(gamesList.get(i).getPlayers().size() == 0) {
				gamesList.remove(gamesList.get(i));
			}
		}
		
		readyPlayers.remove(key);
	}

	public Game getGame(Player key) {
		// Loop through the games and remove that player where present
		for(int i=0;i<gamesList.size();++i) {
			for(int j=0;j<gamesList.get(i).getPlayers().size();++j) {
				if(gamesList.get(i).getPlayers().get(j).getUsername().equals(key.getUsername())) {
					return gamesList.get(i);
				}
			}
		}				
		return null;
	}
}
