package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerPlayerLeft extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getPlayer")
	private Player player;

	public PacketServerPlayerLeft(Player player) {
		this.player = player;
	}
	
}
