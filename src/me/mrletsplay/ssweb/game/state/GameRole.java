package me.mrletsplay.ssweb.game.state;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptEnum;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;

public enum GameRole implements JavaScriptEnum {
	
	LIBERAL(GameParty.LIBERAL),
	STALIN(GameParty.COMMUNIST),
	COMMUNIST(GameParty.COMMUNIST),
	HITLER(GameParty.FASCIST),
	FASCIST(GameParty.FASCIST),
	;
	
	@JSONValue
	@JavaScriptGetter("getParty")
	private GameParty party;

	private GameRole(GameParty party) {
		this.party = party;
	}
	
	public GameParty getParty() {
		return party;
	}
	
}
