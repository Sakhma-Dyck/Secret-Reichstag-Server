package me.mrletsplay.srweb.packet.impl;

import java.util.Map;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.game.state.GameRole;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerStopGame extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getWinner")
	private GameParty winner;
	
	private Map<String, GameRole> roles;

	public PacketServerStopGame(GameParty winner, Map<String, GameRole> roles) {
		this.winner = winner;
		this.roles = roles;
	}
	
	@Override
	public void preSerialize(JSONObject object) {
		super.preSerialize(object);
		JSONObject rs = new JSONObject();
		roles.forEach((k, v) -> {
			rs.put(k, v.toJSON());
		});
		object.put("roles", rs);
	}
	
}
