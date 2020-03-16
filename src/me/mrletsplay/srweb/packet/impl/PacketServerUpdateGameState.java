package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerUpdateGameState extends PacketData {

	@JSONValue
	@JavaScriptGetter("getNewState")
	private GameState newState;

	public PacketServerUpdateGameState(GameState newState) {
		this.newState = newState;
	}
	
}
