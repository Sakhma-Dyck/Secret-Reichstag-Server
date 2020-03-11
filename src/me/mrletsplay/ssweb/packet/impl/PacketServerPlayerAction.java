package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.board.GameBoardAction;
import me.mrletsplay.ssweb.game.state.board.action.GameActionData;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerPlayerAction extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getAction")
	private GameBoardAction action;
	
	@JSONValue
	@JavaScriptGetter("getData")
	private GameActionData data;

	public PacketServerPlayerAction(GameBoardAction action, GameActionData data) {
		this.action = action;
		this.data = data;
	}
	
}
