package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketClientDiscardCard extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setDiscardIndex")
	private int discardIndex;
	
	@JSONConstructor
	private PacketClientDiscardCard() {}
	
	public int getDiscardIndex() {
		return discardIndex;
	}
	
}
