package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

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
