package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerRoomInfo extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getSelfPlayer")
	private Player selfPlayer;
	
	@JSONValue
	@JavaScriptGetter("getRoom")
	private Room room;
	
	public PacketServerRoomInfo(Player selfPlayer, Room room) {
		this.selfPlayer = selfPlayer;
		this.room = room;
		
	}
	
}
