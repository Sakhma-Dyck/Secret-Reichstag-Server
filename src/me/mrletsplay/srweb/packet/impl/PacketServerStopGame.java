package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerStopGame extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getWinner")
	private GameParty winner;

	public PacketServerStopGame(GameParty winner) {
		this.winner = winner;
	}
	
}
