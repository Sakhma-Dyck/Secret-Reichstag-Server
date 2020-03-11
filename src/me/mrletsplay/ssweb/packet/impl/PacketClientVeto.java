package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.JavaScriptSetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketClientVeto extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("isAcceptVeto")
	@JavaScriptSetter("setAcceptVeto")
	private boolean acceptVeto;

	@JSONConstructor
	private PacketClientVeto() {}
	
	public boolean isAcceptVeto() {
		return acceptVeto;
	}
	
}
