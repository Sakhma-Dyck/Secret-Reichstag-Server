package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerEventLogEntry extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getMessage")
	private String message;

	@JSONValue
	@JavaScriptGetter("isChatMessage")
	private boolean chatMessage;
	
	public PacketServerEventLogEntry(String message, boolean chatMessage) {
		this.message = message;
		this.chatMessage = chatMessage;
	}
	
}
