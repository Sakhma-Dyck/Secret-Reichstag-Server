package me.mrletsplay.ssweb.game.state;

import java.util.HashMap;
import java.util.Map;

import me.mrletsplay.ssweb.game.Player;

public class VoteState {
	
	private GameState state;
	private Map<String, Boolean> votes;
	
	public VoteState(GameState state) {
		this.state = state;
		this.votes = new HashMap<>();
	}
	
	public void putVote(Player player, boolean isYes) {
		votes.put(player.getID(), isYes);
	}
	
	public Boolean getVote(Player player) {
		return votes.get(player.getID());
	}
	
	public Map<String, Boolean> getVotes() {
		return votes;
	}
	
	public int getNumYes() {
		return (int) votes.values().stream().filter(b -> b).count();
	}
	
	public int getNumNo() {
		return (int) votes.values().stream().filter(b -> !b).count();
	}
	
	public boolean isComplete() {
		return votes.size() == state.getRoom().getPlayers().size() - state.getDeadPlayers().size();
	}
	
	public boolean isVoteYes() {
		int yesVotes = getNumYes();
		int noVotes = getNumNo();
		return yesVotes > noVotes;
	}
	
}
