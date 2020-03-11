package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.board.action.GameActionData;
import me.mrletsplay.ssweb.packet.JavaScriptSetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketClientPerformAction extends PacketData {
	
	@JSONValue
	@JavaScriptSetter("setData")
	private GameActionData data;
	
	@JSONConstructor
	private PacketClientPerformAction() {}
	
	public GameActionData getData() {
		return data;
	}

}
