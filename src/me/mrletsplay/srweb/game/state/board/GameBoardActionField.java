package me.mrletsplay.srweb.game.state.board;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.JavaScriptSetter;

public class GameBoardActionField implements JavaScriptConvertible {
	
	@JSONValue
	@JavaScriptGetter("getFieldIndex")
	@JavaScriptSetter("setFieldIndex")
	private int fieldIndex;
	
	@JSONValue
	@JavaScriptGetter("getAction")
	@JavaScriptSetter("setAction")
	private GameBoardAction action;
	
	@JSONConstructor
	private GameBoardActionField() {}

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
	
	public boolean isValid(int numFields) {
		return action != null
				&& action != GameBoardAction.WIN
				&& fieldIndex >= 0
				&& fieldIndex < numFields;
	}

}
