package me.mrletsplay.srweb.session;

import me.mrletsplay.srweb.game.Player;

public class PlayerSession {
	
	public static final long VALID_TIME = 1000 * 60 * 60 * 5; // Sessions are valid for 5 hours
	
	private Player player;
	private long expiresAt;
	
	public PlayerSession(Player player) {
		this.player = player;
		this.expiresAt = System.currentTimeMillis() + VALID_TIME;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public long getExpiresAt() {
		return expiresAt;
	}
	
	public boolean hasExpired() {
		return System.currentTimeMillis() > expiresAt;
	}

}
