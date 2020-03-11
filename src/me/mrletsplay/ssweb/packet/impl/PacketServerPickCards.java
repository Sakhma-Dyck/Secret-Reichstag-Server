package me.mrletsplay.ssweb.packet.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.PacketData;
import me.mrletsplay.ssweb.game.state.GamePolicyCard;

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
