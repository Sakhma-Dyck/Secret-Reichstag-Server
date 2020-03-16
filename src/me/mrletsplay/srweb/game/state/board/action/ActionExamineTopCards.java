package me.mrletsplay.srweb.game.state.board.action;

import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.packet.JavaScriptGetter;

public class ActionExamineTopCards extends GameActionData {
	
	@JSONValue
	@JavaScriptGetter("getCards")
	private List<GamePolicyCard> cards;

	public ActionExamineTopCards(List<GamePolicyCard> cards) {
		this.cards = cards;
	}

}
