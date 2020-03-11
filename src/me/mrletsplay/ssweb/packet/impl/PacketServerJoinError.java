package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerJoinError extends PacketData {

	@JSONValue
	@JavaScriptGetter("getMessage")
	private String message;

	public PacketServerJoinError(String message) {
		this.message = message;
	}
	
}
