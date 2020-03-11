package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

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
