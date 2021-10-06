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
	public PacketClientPerformAction() {}
	
	public PacketClientPerformAction(GameActionData data) {
		this.data = data;
	}

	public GameActionData getData() {
		return data;
	}

}
