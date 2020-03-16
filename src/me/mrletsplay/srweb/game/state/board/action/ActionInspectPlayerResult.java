package me.mrletsplay.srweb.game.state.board.action;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class ActionInspectPlayerResult extends PacketData {

	@JSONValue
	@JavaScriptGetter("getParty")
	private GameParty party;

	public ActionInspectPlayerResult(GameParty party) {
		this.party = party;
	}
	
}
