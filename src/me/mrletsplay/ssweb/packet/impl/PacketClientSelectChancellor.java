package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.ssweb.packet.JavaScriptSetter;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class PacketClientSelectChancellor extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setPlayerID")
	private String playerID;
	
	@JSONConstructor
	private PacketClientSelectChancellor() {}
	
	public String getPlayerID() {
		return playerID;
	}

}
