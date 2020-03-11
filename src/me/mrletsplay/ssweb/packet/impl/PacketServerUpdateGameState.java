package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerUpdateGameState extends PacketData {

	@JSONValue
	@JavaScriptGetter("getNewState")
	private GameState newState;

	public PacketServerUpdateGameState(GameState newState) {
		this.newState = newState;
	}
	
}
