package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.PacketData;

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
