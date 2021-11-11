package me.mrletsplay.srweb.game.state.board;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptEnum;
import me.mrletsplay.srweb.packet.JavaScriptGetter;

public enum GameBoardAction implements JavaScriptEnum {
	
	EXAMINE_TOP_CARDS("Examine top cards", "%s must inspect the top three cards"), // Examine top three cards
	EXAMINE_TOP_CARDS_OTHER("Propose another player to inspect top cards", "%s must pick a player to inspect the top three cards"), // Prez picks player that examines top three cards
	KILL_PLAYER("Kill a player", "%s must pick a player to die"), // Kill player (Gulag/Kill)
	PICK_PRESIDENT("Pick the next president", "%s must pick a player to become the next president"), // Prez picks next prez
	INSPECT_PLAYER("Inspect a player", "%s must examine a players identity"), // Examine a players identity card
	BLOCK_PLAYER("Block a player", "%s must choose a player to be unelectable the next turn"), // Pick a player that will be unelectable the next turn
	
	WIN(null, null);
	
	@JSONValue
	@JavaScriptGetter("getFriendlyName")
	private String friendlyName;
	
	@JSONValue
	@JavaScriptGetter("getEventLogMessage")
	private String eventLogMessage;

	private GameBoardAction(String friendlyName, String eventLogMessage) {
		this.friendlyName = friendlyName;
		this.eventLogMessage = eventLogMessage;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
	public String getEventLogMessage() {
		return eventLogMessage;
	}
	
}
