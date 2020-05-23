package me.mrletsplay.srweb.game;

import me.mrletsplay.srweb.packet.JavaScriptEnum;

public enum GameMode implements JavaScriptEnum {
	
	SECRET_HITLER(5, 10),
	SECRET_REICHSTAG(7, 14);
	
	private final int
		minPlayers,
		maxPlayers;

	private GameMode(int minPlayers, int maxPlayers) {
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
}
