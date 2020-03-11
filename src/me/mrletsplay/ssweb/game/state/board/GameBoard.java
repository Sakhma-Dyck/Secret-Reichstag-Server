package me.mrletsplay.ssweb.game.state.board;

import java.util.Arrays;
import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.packet.JavaScriptConvertible;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;

public class GameBoard implements JavaScriptConvertible {
	
	@JSONValue
	@JavaScriptGetter("getNumCards")
	private int numCards;
	
	@JSONValue
	@JavaScriptGetter("getMaxCards")
	private int maxCards;
	
	@JSONValue
	@JavaScriptGetter("getActionFields")
	private List<GameBoardActionField> actionFields;
	
	public GameBoard(int maxCards, GameBoardActionField... actionFields) {
		this.maxCards = maxCards;
		this.actionFields = Arrays.asList(actionFields);
	}
	
	public GameBoardAction addCard() {
		GameBoardActionField f = getActionField(numCards);
		numCards++;
		if(numCards == maxCards) return GameBoardAction.WIN;
		return f == null ? null : f.getAction();
	}
	
	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
	
	public int getNumCards() {
		return numCards;
	}
	
	public int getMaxCards() {
		return maxCards;
	}
	
	public GameBoardActionField getActionField(int index) {
		return actionFields.stream()
				.filter(f -> f.getFieldIndex() == index)
				.findFirst().orElse(null);
	}
	
	public List<GameBoardActionField> getActionFields() {
		return actionFields;
	}

}
