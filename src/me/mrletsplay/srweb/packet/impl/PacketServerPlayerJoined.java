package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerPlayerJoined extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getPlayer")
	private Player player;
	
	@JSONValue
	@JavaScriptGetter("isRejoin")
	private boolean rejoin;

	public PacketServerPlayerJoined(Player player, boolean rejoin) {
		this.player = player;
		this.rejoin = rejoin;
	}
	
}
