package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerPlayerLeft extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getPlayer")
	private Player player;
	
	@JSONValue
	@JavaScriptGetter("isHardLeave")
	private boolean hardLeave;

	public PacketServerPlayerLeft(Player player, boolean hardLeave) {
		this.player = player;
	}
	
}
