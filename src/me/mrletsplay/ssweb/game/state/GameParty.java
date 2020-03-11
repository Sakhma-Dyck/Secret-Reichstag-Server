package me.mrletsplay.ssweb.game.state;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptEnum;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;

public enum GameParty implements JavaScriptEnum {
	
	LIBERAL("Liberals", "Liberal", null),
	COMMUNIST("Communists", "Communist", "Stalin"),
	FASCIST("Fascists", "Fascist", "Hitler"),
	;
	
	@JSONValue
	@JavaScriptGetter("getFriendlyName")
	private String friendlyName;
	
	@JSONValue
	@JavaScriptGetter("getFriendlyNameSingular")
	private String friendlyNameSingular;
	
	@JSONValue
	@JavaScriptGetter("getLeaderName")
	private String leaderName;

	private GameParty(String friendlyName, String friendlyNameSingular, String leaderName) {
		this.friendlyName = friendlyName;
		this.friendlyNameSingular = friendlyNameSingular;
		this.leaderName = leaderName;
	}

	public String getFriendlyName() {
		return friendlyName;
	}
	
	public String getFriendlyNameSingular() {
		return friendlyNameSingular;
	}
	
	public String getLeaderName() {
		return leaderName;
	}
	
}
