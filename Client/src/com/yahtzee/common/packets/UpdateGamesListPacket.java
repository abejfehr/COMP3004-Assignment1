package com.yahtzee.common.packets;

import java.util.HashMap;

public class UpdateGamesListPacket extends Packet {
	public HashMap<String, Integer> gamesList;
	
	public UpdateGamesListPacket() {
		// Create an empty hashmap at first
		gamesList = new HashMap<String, Integer>();
	}
	
	public void setGamesList(HashMap<String, Integer> gamesList) {
		this.gamesList = gamesList;
	}
}
