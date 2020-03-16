package me.mrletsplay.srweb.game.state.board;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptEnum;
import me.mrletsplay.srweb.packet.JavaScriptGetter;

public enum GameBoardAction implements JavaScriptEnum {
	
	EXAMINE_TOP_CARDS("%s must inspect the top three cards"), // Examine top three cards
	EXAMINE_TOP_CARDS_OTHER("%s must pick a player to inspect the top three cards"), // Prez picks player that examines top three cards
	KILL_PLAYER("%s must pick a player to die"), // Kill player (Gulag/Kill)
	PICK_PRESIDENT("%s must pick a player to become the next president"), // Prez picks next prez
	INSPECT_PLAYER("%s must examine a palyers identity"), // Examine a players identity card
	BLOCK_PLAYER("%s must choose a player to be unelectable the next turn"), // Pick a player that will be unelectable the next turn
	
	WIN(null);
	
	@JSONValue
	@JavaScriptGetter("getEventLogMessage")
	private String eventLogMessage;

	private GameBoardAction(String eventLogMessage) {
		this.eventLogMessage = eventLogMessage;
	}
	
	public String getEventLogMessage() {
		return eventLogMessage;
	}
	
}
