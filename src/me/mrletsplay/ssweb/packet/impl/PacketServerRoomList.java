package me.mrletsplay.ssweb.packet.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONComplexListType;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerRoomList extends PacketData {
	
	@JSONValue
	@JSONComplexListType(Room.class)
	@JavaScriptGetter("getRooms")
	private List<Room> rooms;
	
	public PacketServerRoomList(List<Room> rooms) {
		this.rooms = rooms;
	}
	
}
