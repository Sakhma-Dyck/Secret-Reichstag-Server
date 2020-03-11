package me.mrletsplay.ssweb.game;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptConvertible;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.JavaScriptSetter;

public class RoomSettings implements JavaScriptConvertible {

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
		this.playerCount = 7;
		this.liberalCardCount = 9;
		this.communistCardCount = 11;
		this.fascistCardCount = 11;
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
		return
				playerCount >= 7 && playerCount <= 14 &&
				liberalCardCount >= 5 && liberalCardCount <= 15 &&
				communistCardCount >= 6 && communistCardCount <= 15 &&
				fascistCardCount >= 6 && fascistCardCount <= 15;
	}
	
}
