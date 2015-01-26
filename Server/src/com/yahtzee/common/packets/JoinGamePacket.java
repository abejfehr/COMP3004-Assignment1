package com.yahtzee.common.packets;

import com.yahtzee.common.model.Player;

public class JoinGamePacket extends Packet {
	public String gameTitle;
	public Player player;
}
