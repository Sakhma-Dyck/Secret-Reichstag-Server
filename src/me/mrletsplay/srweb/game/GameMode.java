package me.mrletsplay.srweb.game;

import me.mrletsplay.srweb.packet.JavaScriptEnum;

public enum GameMode implements JavaScriptEnum {
	
	SECRET_HITLER(5, 10, 7),
	SECRET_REICHSTAG(7, 14, 10);
	
	private final int
		minPlayers,
		maxPlayers,
		minInvisible;

	private GameMode(int minPlayers, int maxPlayers, int minInvisible) {
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.minInvisible = minInvisible;
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
	
}
