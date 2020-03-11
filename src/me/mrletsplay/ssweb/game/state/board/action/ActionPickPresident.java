package me.mrletsplay.ssweb.game.state.board.action;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptSetter;

public class ActionPickPresident extends GameActionData {
	
	@JSONValue
	@JavaScriptSetter("setPlayerID")
	private String playerID;
	
	@JSONConstructor
	private ActionPickPresident() {}

	public String getPlayerID() {
		return playerID;
	}
	
}
