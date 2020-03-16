package me.mrletsplay.srweb.packet.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.PacketData;

public class PacketServerPickCards extends PacketData {
	
	@JSONValue
	@JavaScriptGetter("getCards")
	private List<GamePolicyCard> cards;
	
	@JSONValue
	@JavaScriptGetter("isVetoBlocked")
	private boolean vetoBlocked;

	public PacketServerPickCards(List<GamePolicyCard> cards, boolean vetoBlocked) {
		this.cards = cards;
		this.vetoBlocked = vetoBlocked;
	}

}
