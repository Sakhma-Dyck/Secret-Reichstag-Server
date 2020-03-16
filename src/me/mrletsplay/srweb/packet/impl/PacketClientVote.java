package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketClientVote extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setYes")
	private boolean yes;
	
	@JSONConstructor
	private PacketClientVote() {}

	public boolean isYes() {
		return yes;
	}
	
}
