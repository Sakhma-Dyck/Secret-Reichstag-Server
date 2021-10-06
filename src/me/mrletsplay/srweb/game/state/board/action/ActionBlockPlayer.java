package me.mrletsplay.srweb.game.state.board.action;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptSetter;

public class ActionBlockPlayer extends GameActionData {
	
	@JSONValue
	@JavaScriptSetter("setPlayerID")
	private String playerID;
	
	@JSONConstructor
	private ActionBlockPlayer() {}
	
	public ActionBlockPlayer(String playerID) {
		this.playerID = playerID;
	}

	public String getPlayerID() {
		return playerID;
	}
	
}
