package me.mrletsplay.srweb.packet.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONComplexListType;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerRoomList extends PacketData {
	
	@JSONValue
	@JSONComplexListType(Room.class)
	@JavaScriptGetter("getRooms")
	private List<Room> rooms;
	
	public PacketServerRoomList(List<Room> rooms) {
		this.rooms = rooms;
	}
	
}
