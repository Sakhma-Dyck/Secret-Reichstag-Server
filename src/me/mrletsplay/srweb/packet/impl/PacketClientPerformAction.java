package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.board.action.GameActionData;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.PacketData;

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
