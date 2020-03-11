package me.mrletsplay.ssweb.packet.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.VoteState;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;

public class PacketServerVoteResults extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getVotes")
	private JSONObject votes;
	
	public PacketServerVoteResults(VoteState voteState) {
		this.votes = new JSONObject(voteState.getVotes());
	}

}
