package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerJoinError extends PacketData {

	@JSONValue
	@JavaScriptGetter("getMessage")
	private String message;

	public PacketServerJoinError(String message) {
		this.message = message;
	}
	
}
