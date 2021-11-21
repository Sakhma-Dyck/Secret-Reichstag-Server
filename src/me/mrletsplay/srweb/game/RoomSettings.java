package me.mrletsplay.srweb.game;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONComplexListType;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.board.GameBoardActionField;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.JavaScriptSetter;

public class RoomSettings implements JavaScriptConvertible {
	
	@JSONValue
	@JavaScriptSetter("setMode")
	@JavaScriptGetter("getMode")
	private String mode;

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
	
	@JSONValue
	@JSONComplexListType(GameBoardActionField.class)
	@JavaScriptGetter("getLiberalBoard")
	@JavaScriptSetter("setLiberalBoard")
	private List<GameBoardActionField> liberalBoard;
	
	@JSONValue
	@JSONComplexListType(GameBoardActionField.class)
	@JavaScriptGetter("getFascistBoard")
	@JavaScriptSetter("setFascistBoard")
	private List<GameBoardActionField> fascistBoard;
	
	@JSONValue
	@JSONComplexListType(GameBoardActionField.class)
	@JavaScriptGetter("getCommunistBoard")
	@JavaScriptSetter("setCommunistBoard")
	private List<GameBoardActionField> communistBoard;
	
	@JSONConstructor
	public RoomSettings() {
		this.mode = GameMode.SECRET_REICHSTAG.name();
		this.liberalCardCount = 9;
		this.communistCardCount = 11;
		this.fascistCardCount = 11;
		this.liberalBoard = null;
		this.fascistBoard = null;
		this.communistBoard = null;
	}
	
	public String getMode() {
		return mode;
	}
	
	public int getMinPlayerCount() {
		GameMode m = GameMode.valueOf(mode);
		return m.getMinPlayers();
	}
	
	public int getMaxPlayerCount() {
		GameMode m = GameMode.valueOf(mode);
		return m.getMaxPlayers();
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
	
	public List<GameBoardActionField> getLiberalBoard() {
		return liberalBoard;
	}
	
	public List<GameBoardActionField> getFascistBoard() {
		return fascistBoard;
	}
	
	public List<GameBoardActionField> getCommunistBoard() {
		return communistBoard;
	}
	
	public boolean isValid() {
		try {
			GameMode m = GameMode.valueOf(mode);
			return liberalCardCount >= 5 && liberalCardCount <= 15 &&
					((communistCardCount >= 6 && communistCardCount <= 15) || m == GameMode.SECRET_HITLER) &&
					fascistCardCount >= 6 && fascistCardCount <= 15
					&& (liberalBoard == null || liberalBoard.stream().allMatch(f -> f.isValid(5)))
					&& (fascistBoard == null || fascistBoard.stream().allMatch(f -> f.isValid(6)))
					&& (communistBoard == null || communistBoard.stream().allMatch(f -> f.isValid(6)));
		}catch(IllegalArgumentException | NullPointerException e) {
			return false;
		}
	}
	
}
