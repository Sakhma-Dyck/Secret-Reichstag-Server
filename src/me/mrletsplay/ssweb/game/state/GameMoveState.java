package me.mrletsplay.ssweb.game.state;

import me.mrletsplay.ssweb.packet.JavaScriptEnum;

public enum GameMoveState implements JavaScriptEnum {
	
	SELECT_CHANCELLOR,
	VOTE,
	DRAW_CARDS,
	DISCARD_PRESIDENT,
	DISCARD_CHANCELLOR,
	ACTION,

}
