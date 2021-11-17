package me.mrletsplay.srweb.game.bot.strategy;

import java.util.List;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.bot.BotPlayer;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.game.state.GameState;

public abstract class AbstractStrategy {
	
	private BotPlayer bot;
	
	public AbstractStrategy(BotPlayer bot) {
		this.bot = bot;
	}
	
	public BotPlayer getBot() {
		return bot;
	}
	
	public abstract String selectChancellor(GameState state, List<Player> potentialPlayers);
	
	public abstract boolean vote(GameState state);
	
	public abstract int pickCard(GameState state, List<GamePolicyCard> cards);
	
	public abstract String actionBlockPlayer(GameState state, List<Player> potentialPlayers);
	
	public abstract void actionExamineTopCards(GameState state, List<GamePolicyCard> cards);
	
	public abstract String actionExamineTopCardsOther(GameState state, List<Player> potentialPlayers);
	
	public abstract String actionInspectPlayer(GameState state, List<Player> potentialPlayers);
	
	public abstract void actionInspectPlayerResult(GameState state, GameParty playerParty);
	
	public abstract String actionKillPlayer(GameState state, List<Player> potentialPlayers);
	
	public abstract String actionPickPresident(GameState state, List<Player> potentialPlayers);
	
	public abstract boolean onVeto(GameState state);
	
}
