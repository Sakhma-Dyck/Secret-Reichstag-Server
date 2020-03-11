package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.GameParty;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerStopGame extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getWinner")
	private GameParty winner;

	public PacketServerStopGame(GameParty winner) {
		this.winner = winner;
	}
	
}
