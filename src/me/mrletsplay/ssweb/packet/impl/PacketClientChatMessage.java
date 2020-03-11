package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.ssweb.packet.JavaScriptSetter;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class PacketClientChatMessage extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setMessage")
	private String message;
	
	@JSONConstructor
	private PacketClientChatMessage() {}

	public String getMessage() {
		return message;
	}
	
	public boolean isValid() {
		return message != null && message.length() <= 128;
	}
	
}
