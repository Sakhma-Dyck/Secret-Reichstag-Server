package me.mrletsplay.ssweb.game.state.board.action;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.GameParty;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class ActionInspectPlayerResult extends PacketData {

	@JSONValue
	@JavaScriptGetter("getParty")
	private GameParty party;

	public ActionInspectPlayerResult(GameParty party) {
		this.party = party;
	}
	
}
