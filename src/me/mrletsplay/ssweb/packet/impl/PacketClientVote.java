package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptSetter;
import me.mrletsplay.ssweb.packet.PacketData;

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
