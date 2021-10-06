package me.mrletsplay.srweb.game;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptEnum;
import me.mrletsplay.srweb.packet.JavaScriptGetter;

public enum GameMode implements JavaScriptEnum {
	
	SECRET_HITLER(5, 10, 7, 6),
	SECRET_REICHSTAG(7, 14, 10, 8);
	
	@JSONValue
	@JavaScriptGetter("getMinPlayers")
	private final int minPlayers;
	
	@JSONValue
	@JavaScriptGetter("getMaxPlayers")
	private final int maxPlayers;
	
	private final int
		minInvisible, // Minimum number of players required for Hitler/Stalin to not see their colleagues
		minPrevPresident; // Minimum number of players required to block "Previous President"s from being proposed as chancellor

	private GameMode(int minPlayers, int maxPlayers, int minInvisible, int minPrevPresident) {
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.minInvisible = minInvisible;
		this.minPrevPresident = minPrevPresident;
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getMinInvisible() {
		return minInvisible;
	}
	
	public int getMinPrevPresident() {
		return minPrevPresident;
	}
	
}
