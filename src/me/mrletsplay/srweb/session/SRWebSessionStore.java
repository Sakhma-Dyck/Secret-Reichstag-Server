package me.mrletsplay.srweb.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.mrletsplay.srweb.game.Player;

public class SRWebSessionStore {
	
	private static Map<String, PlayerSession> activeSessions = new HashMap<>();
	
	public static String createSession(Player player) {
		String sessionID = UUID.randomUUID().toString();
		activeSessions.put(sessionID, new PlayerSession(player));
		return sessionID;
	}
	
	public static PlayerSession getSession(String sessionID) {
		System.out.println(activeSessions);
		updateSessions();
		System.out.println("a: " + activeSessions);
		return activeSessions.get(sessionID);
	}
	
	public static void updateSessions() {
		activeSessions.values().removeIf(PlayerSession::hasExpired);
	}
	
}
