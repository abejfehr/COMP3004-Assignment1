package com.yahtzee.common.packets;

import java.util.ArrayList;

import com.yahtzee.common.model.Player;

public class PlayerJoinedGamePacket extends Packet {
	public ArrayList<Player> players;
	public String messageText; // The pre-existing message text from the chat
}