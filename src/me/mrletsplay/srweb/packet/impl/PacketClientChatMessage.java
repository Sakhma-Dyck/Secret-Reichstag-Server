package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketClientChatMessage extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setMessage")
	private String message;
	
	@JSONConstructor
	private PacketClientChatMessage() {}
	
	public PacketClientChatMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public boolean isValid() {
		return message != null && message.length() <= 128;
	}
	
}
