package com.yahtzee.common.packets;

import com.yahtzee.common.model.Player;

public class SendMessagePacket extends Packet {
	public String message;
	public String gameTitle;
	public Player sendingPlayer;
}
