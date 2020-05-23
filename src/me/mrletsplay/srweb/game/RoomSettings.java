package me.mrletsplay.srweb.game;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.JavaScriptSetter;

public class RoomSettings implements JavaScriptConvertible {
	
	@JSONValue
	@JavaScriptSetter("setMode")
	@JavaScriptGetter("getMode")
	private String mode;

	@JSONValue
	@JavaScriptSetter("setPlayerCount")
	@JavaScriptGetter("getPlayerCount")
	private int playerCount;
	
	@JSONValue
	@JavaScriptSetter("setLiberalCardCount")
	@JavaScriptGetter("getLiberalCardCount")
	private int liberalCardCount;
	
	@JSONValue
	@JavaScriptSetter("setCommunistCardCount")
	@JavaScriptGetter("getCommunistCardCount")
	private int communistCardCount;
	
	@JSONValue
	@JavaScriptSetter("setFascistCardCount")
	@JavaScriptGetter("getFascistCardCount")
	private int fascistCardCount;
	
	@JSONConstructor
	public RoomSettings() {
		this.mode = GameMode.SECRET_REICHSTAG.name();
		this.playerCount = 7;
		this.liberalCardCount = 9;
		this.communistCardCount = 11;
		this.fascistCardCount = 11;
	}
	
	public String getMode() {
		return mode;
	}
	
	public int getPlayerCount() {
		return playerCount;
	}
	
	public int getLiberalCardCount() {
		return liberalCardCount;
	}
	
	public int getCommunistCardCount() {
		return communistCardCount;
	}
	
	public int getFascistCardCount() {
		return fascistCardCount;
	}
	
	public boolean isValid() {
		try {
			GameMode m = GameMode.valueOf(mode);
			return playerCount >= m.getMinPlayers() && playerCount <= m.getMaxPlayers() &&
					liberalCardCount >= 5 && liberalCardCount <= 15 &&
					((communistCardCount >= 6 && communistCardCount <= 15) || m == GameMode.SECRET_HITLER) &&
					fascistCardCount >= 6 && fascistCardCount <= 15;
		}catch(IllegalArgumentException | NullPointerException e) {
			return false;
		}
	}
	
}
