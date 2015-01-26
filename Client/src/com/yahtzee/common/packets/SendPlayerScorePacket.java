package com.yahtzee.common.packets;

import com.yahtzee.common.model.Player;

public class SendPlayerScorePacket extends Packet {
	public int category;
	public int score;
	public String gameTitle;
	public Player player;
}
