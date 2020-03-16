package me.mrletsplay.srweb.packet.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.VoteState;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerVoteResults extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getVotes")
	private JSONObject votes;
	
	public PacketServerVoteResults(VoteState voteState) {
		this.votes = new JSONObject(voteState.getVotes());
	}

}
