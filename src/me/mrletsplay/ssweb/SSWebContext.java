package me.mrletsplay.ssweb;

import me.mrletsplay.ssweb.game.Player;

public class SSWebContext {

	private static ThreadLocal<Player> currentPlayer = new ThreadLocal<>();
	
	private SSWebContext() {}
	
	public static void setCurrentPlayer(Player player) {
		currentPlayer.set(player);
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer.get();
	}
	
}
