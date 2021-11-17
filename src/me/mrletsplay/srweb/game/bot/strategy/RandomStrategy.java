package me.mrletsplay.srweb.game.bot.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.bot.BotPlayer;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.game.state.GameState;

public class RandomStrategy extends AbstractStrategy {

	private Random random = new Random();

	public RandomStrategy(BotPlayer bot) {
		super(bot);
	}
	
	@Override
	public String selectChancellor(GameState state, List<Player> potentialPlayers) {
		return potentialPlayers.get(random.nextInt(potentialPlayers.size())).getID();
	}

	@Override
	public boolean vote(GameState state) {
		return random.nextBoolean();
	}

	@Override
	public int pickCard(GameState state, List<GamePolicyCard> cards) {
		return random.nextInt(cards.size());
	}

	@Override
	public String actionBlockPlayer(GameState state, List<Player> potentialPlayers) {
		System.out.println(potentialPlayers);
		return potentialPlayers.get(random.nextInt(potentialPlayers.size())).getID();
	}

	@Override
	public void actionExamineTopCards(GameState state, List<GamePolicyCard> cards) {
		List<GamePolicyCard> randomCards = new ArrayList<>();
		for(int i = 0; i < 3; i++) randomCards.add(GamePolicyCard.values()[random.nextInt(GamePolicyCard.values().length)]);
		getBot().chat("The top 3 cards are: " + randomCards);
	}

	@Override
	public String actionExamineTopCardsOther(GameState state, List<Player> potentialPlayers) {
		return potentialPlayers.get(random.nextInt(potentialPlayers.size())).getID();
	}

	@Override
	public String actionInspectPlayer(GameState state, List<Player> potentialPlayers) {
		Player p = potentialPlayers.get(random.nextInt(potentialPlayers.size()));
		getBot().chat("I will inspect " + p.getName());
		return p.getID();
	}

	@Override
	public void actionInspectPlayerResult(GameState state, GameParty playerParty) {
		getBot().chat("They are a " + GameParty.values()[random.nextInt(GameParty.values().length)].getFriendlyNameSingular());
	}

	@Override
	public String actionKillPlayer(GameState state, List<Player> potentialPlayers) {
		return potentialPlayers.get(random.nextInt(potentialPlayers.size())).getID();
	}

	@Override
	public String actionPickPresident(GameState state, List<Player> potentialPlayers) {
		return potentialPlayers.get(random.nextInt(potentialPlayers.size())).getID();
	}
	
	@Override
	public boolean onVeto(GameState state) {
		return random.nextBoolean();
	}

}
