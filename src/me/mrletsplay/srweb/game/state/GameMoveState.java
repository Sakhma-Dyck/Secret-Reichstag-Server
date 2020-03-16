package me.mrletsplay.srweb.game.state;

import me.mrletsplay.srweb.packet.JavaScriptEnum;

public enum GameMoveState implements JavaScriptEnum {
	
	SELECT_CHANCELLOR,
	VOTE,
	DRAW_CARDS,
	DISCARD_PRESIDENT,
	DISCARD_CHANCELLOR,
	ACTION,

}
