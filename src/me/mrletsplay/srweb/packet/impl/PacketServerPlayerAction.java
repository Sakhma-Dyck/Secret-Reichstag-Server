package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.board.GameBoardAction;
import me.mrletsplay.srweb.game.state.board.action.GameActionData;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

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
