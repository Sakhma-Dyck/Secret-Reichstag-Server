package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerPlayerLeft extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getPlayer")
	private Player player;

	public PacketServerPlayerLeft(Player player) {
		this.player = player;
	}
	
}
