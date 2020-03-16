package me.mrletsplay.srweb.packet.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.state.GameRole;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerStartGame extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getRole")
	private GameRole role;
	
	@JSONValue
	@JavaScriptGetter("getLeader")
	private Player leader;
	
	@JSONValue
	@JavaScriptGetter("getTeammates")
	private List<Player> teammates;

	public PacketServerStartGame(GameRole role, Player leader, List<Player> teammates) {
		this.role = role;
		this.leader = leader;
		this.teammates = teammates;
	}
	
}
