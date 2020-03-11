package me.mrletsplay.ssweb.game.state.board;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptConvertible;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;

public class GameBoardActionField implements JavaScriptConvertible {
	
	@JSONValue
	@JavaScriptGetter("getFieldIndex")
	private int fieldIndex;
	
	@JSONValue
	@JavaScriptGetter("getAction")
	private GameBoardAction action;

	public GameBoardActionField(int fieldIndex, GameBoardAction action) {
		this.fieldIndex = fieldIndex;
		this.action = action;
	}

	public int getFieldIndex() {
		return fieldIndex;
	}

	public GameBoardAction getAction() {
		return action;
	}

}
