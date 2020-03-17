package me.mrletsplay.srweb.util;

import me.mrletsplay.srweb.game.Player;

public class SRWebContext {

	private static ThreadLocal<Player> currentPlayer = new ThreadLocal<>();
	
	private SRWebContext() {}
	
	public static void setCurrentPlayer(Player player) {
		currentPlayer.set(player);
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer.get();
	}
	
}
