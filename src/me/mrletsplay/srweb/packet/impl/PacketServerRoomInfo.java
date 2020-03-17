package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerRoomInfo extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getSessionID")
	private String sessionID;
	
	@JSONValue
	@JavaScriptGetter("getSelfPlayer")
	private Player selfPlayer;
	
	@JSONValue
	@JavaScriptGetter("getRoom")
	private Room room;
	
	public PacketServerRoomInfo(String sessionID, Player selfPlayer, Room room) {
		this.sessionID = sessionID;
		this.selfPlayer = selfPlayer;
		this.room = room;
	}
	
}
