package me.mrletsplay.srweb.game.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.game.GameMode;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.board.GameBoard;
import me.mrletsplay.srweb.game.state.board.GameBoardAction;
import me.mrletsplay.srweb.game.state.board.GameBoardActionField;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.JavaScriptGetter;

public class GameState implements JavaScriptConvertible {
	
	private Room room;
	
	@JSONValue
	@JavaScriptGetter("getLiberalBoard")
	private GameBoard liberalBoard;
	
	@JSONValue
	@JavaScriptGetter("getCommunistBoard")
	private GameBoard communistBoard;
	
	@JSONValue
	@JavaScriptGetter("getFascistBoard")
	private GameBoard fascistBoard;
	
	private List<GamePolicyCard> drawPile, discardPile;
	
	@JSONValue
	@JavaScriptGetter("getMoveState")
	private GameMoveState moveState;
	
	private int presidentIndex;
	
	private List<Player> liberals;
	
	private Player stalin;
	
	private List<Player> communists;
	
	private Player hitler;
	
	private List<Player> fascists;
	
	@JSONValue
	@JavaScriptGetter("getPresident")
	private Player president;
	
	@JSONValue
	@JavaScriptGetter("getChancellor")
	private Player chancellor;
	
	@JSONValue
	@JavaScriptGetter("getPreviousPresident")
	private Player previousPresident;
	
	@JSONValue
	@JavaScriptGetter("getPreviousChancellor")
	private Player previousChancellor;
	
	@JSONValue
	@JavaScriptGetter("getBlockedPlayer")
	private Player blockedPlayer;
	
	@JSONValue
	@JavaScriptGetter("getActionPerformer")
	private Player actionPerformer;
	
	@JSONValue
	@JavaScriptGetter("getAction")
	private GameBoardAction action;
	
	private VoteState voteState;
	
	@JSONValue
	@JavaScriptGetter("getDeadPlayers")
	private List<Player> deadPlayers;
	
	@JSONValue
	@JavaScriptGetter("getFailedElections")
	private int failedElections;
	
	@JSONValue
	@JavaScriptGetter("isVetoPowerUnlocked")
	private boolean vetoPowerUnlocked;
	
	@JSONValue
	@JavaScriptGetter("isVetoRequested")
	private boolean vetoRequested;
	
	@JSONValue
	@JavaScriptGetter("isVetoBlocked")
	private boolean vetoBlocked;

	@JSONValue
	@JavaScriptGetter("getNotStalinConfirmed")
	private List<Player> notStalinConfirmed;
	
	@JSONValue
	@JavaScriptGetter("getNotHitlerConfirmed")
	private List<Player> notHitlerConfirmed;
	
	public GameState(Room room) {
		this.room = room;
		this.liberalBoard = new GameBoard(5);
		
		int numPlayers = room.getSettings().getPlayerCount();
		
		if(room.getMode() == GameMode.SECRET_REICHSTAG) {
			if(numPlayers <= 8) {
				this.communistBoard = new GameBoard(6,
						new GameBoardActionField(1, GameBoardAction.EXAMINE_TOP_CARDS_OTHER),
						new GameBoardActionField(2, GameBoardAction.PICK_PRESIDENT),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
				this.fascistBoard = new GameBoard(6,
						new GameBoardActionField(1, GameBoardAction.EXAMINE_TOP_CARDS),
						new GameBoardActionField(2, GameBoardAction.BLOCK_PLAYER),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
			}else if(numPlayers >= 9) {
				this.communistBoard = new GameBoard(6,
						new GameBoardActionField(0, GameBoardAction.INSPECT_PLAYER),
						new GameBoardActionField(1, GameBoardAction.EXAMINE_TOP_CARDS),
						new GameBoardActionField(2, GameBoardAction.EXAMINE_TOP_CARDS_OTHER),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
				this.fascistBoard = new GameBoard(6,
						new GameBoardActionField(0, GameBoardAction.INSPECT_PLAYER),
						new GameBoardActionField(1, GameBoardAction.PICK_PRESIDENT),
						new GameBoardActionField(2, GameBoardAction.BLOCK_PLAYER),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
			}
		}else {
			if(numPlayers <= 6) {
				this.fascistBoard = new GameBoard(6,
						new GameBoardActionField(2, GameBoardAction.EXAMINE_TOP_CARDS),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
			}else if(numPlayers <= 8) {
				this.fascistBoard = new GameBoard(6,
						new GameBoardActionField(1, GameBoardAction.INSPECT_PLAYER),
						new GameBoardActionField(2, GameBoardAction.PICK_PRESIDENT),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
			}else {
				this.fascistBoard = new GameBoard(6,
						new GameBoardActionField(0, GameBoardAction.INSPECT_PLAYER),
						new GameBoardActionField(1, GameBoardAction.INSPECT_PLAYER),
						new GameBoardActionField(2, GameBoardAction.PICK_PRESIDENT),
						new GameBoardActionField(3, GameBoardAction.KILL_PLAYER),
						new GameBoardActionField(4, GameBoardAction.KILL_PLAYER));
			}
		}
		
		this.drawPile = new ArrayList<>();
		for(int i = 0; i < room.getSettings().getCommunistCardCount(); i++) drawPile.add(GamePolicyCard.COMMUNIST);
		for(int i = 0; i < room.getSettings().getFascistCardCount(); i++) drawPile.add(GamePolicyCard.FASCIST);
		for(int i = 0; i < room.getSettings().getLiberalCardCount(); i++) drawPile.add(GamePolicyCard.LIBERAL);
		Collections.shuffle(drawPile);
		
		this.discardPile = new ArrayList<>();
		
		this.liberals = new ArrayList<>();
		this.communists = new ArrayList<>();
		this.fascists = new ArrayList<>();
		this.deadPlayers = new ArrayList<>();
		this.notStalinConfirmed = new ArrayList<>();
		this.notHitlerConfirmed = new ArrayList<>();
	}
	
	public Room getRoom() {
		return room;
	}
	
	public GameBoard getLiberalBoard() {
		return liberalBoard;
	}
	
	public GameBoard getCommunistBoard() {
		return communistBoard;
	}
	
	public GameBoard getFascistBoard() {
		return fascistBoard;
	}
	
	public List<GamePolicyCard> getDrawPile() {
		return drawPile;
	}
	
	public List<GamePolicyCard> getDiscardPile() {
		return discardPile;
	}
	
	public void setMoveState(GameMoveState moveState) {
		this.moveState = moveState;
	}
	
	public GameMoveState getMoveState() {
		return moveState;
	}
	
	public void setLiberals(List<Player> liberals) {
		this.liberals = liberals;
	}
	
	public List<Player> getLiberals() {
		return liberals;
	}
	
	public void setStalin(Player stalin) {
		this.stalin = stalin;
	}
	
	public Player getStalin() {
		return stalin;
	}
	
	public void setCommunists(List<Player> communists) {
		this.communists = communists;
	}
	
	public List<Player> getCommunists() {
		return communists;
	}
	
	public void setHitler(Player hitler) {
		this.hitler = hitler;
	}
	
	public Player getHitler() {
		return hitler;
	}
	
	public void setFascists(List<Player> fascists) {
		this.fascists = fascists;
	}
	
	public List<Player> getFascists() {
		return fascists;
	}
	
	public void setPresidentIndex(int presidentIndex) {
		this.presidentIndex = presidentIndex;
	}
	
	public int getPresidentIndex() {
		return presidentIndex;
	}
	
	public void setPresident(Player president) {
		this.president = president;
	}
	
	public Player getPresident() {
		return president;
	}
	
	public void setChancellor(Player chancellor) {
		this.chancellor = chancellor;
	}
	
	public Player getChancellor() {
		return chancellor;
	}
	
	public void setPreviousPresident(Player previousPresident) {
		this.previousPresident = previousPresident;
	}
	
	public Player getPreviousPresident() {
		return previousPresident;
	}
	
	public void setPreviousChancellor(Player previousChancellor) {
		this.previousChancellor = previousChancellor;
	}
	
	public Player getPreviousChancellor() {
		return previousChancellor;
	}
	
	public void setBlockedPlayer(Player blockedPlayer) {
		this.blockedPlayer = blockedPlayer;
	}
	
	public Player getBlockedPlayer() {
		return blockedPlayer;
	}
	
	public void setAction(GameBoardAction action) {
		this.action = action;
	}
	
	public GameBoardAction getAction() {
		return action;
	}
	
	public void setVoteState(VoteState voteState) {
		this.voteState = voteState;
	}
	
	public VoteState getVoteState() {
		return voteState;
	}
	
	public void setActionPerformer(Player actionPerformer) {
		this.actionPerformer = actionPerformer;
	}
	
	public Player getActionPerformer() {
		return actionPerformer;
	}
	
	public void addDeadPlayer(Player player) {
		deadPlayers.add(player);
	}
	
	public List<Player> getDeadPlayers() {
		return deadPlayers;
	}
	
	public void setFailedElections(int failedElections) {
		this.failedElections = failedElections;
	}
	
	public void addFailedElection() {
		failedElections++;
	}
	
	public void resetFailedElections() {
		failedElections = 0;
	}
	
	public int getFailedElections() {
		return failedElections;
	}
	
	public void setVetoPowerUnlocked(boolean vetoPowerUnlocked) {
		this.vetoPowerUnlocked = vetoPowerUnlocked;
	}
	
	public boolean isVetoPowerUnlocked() {
		return vetoPowerUnlocked;
	}
	
	public void setVetoRequested(boolean vetoRequested) {
		this.vetoRequested = vetoRequested;
	}
	
	public boolean isVetoRequested() {
		return vetoRequested;
	}
	
	public void setVetoBlocked(boolean vetoBlocked) {
		this.vetoBlocked = vetoBlocked;
	}
	
	public boolean isVetoBlocked() {
		return vetoBlocked;
	}
	
	public void addNotStalinConfirmed(Player player) {
		if(notStalinConfirmed.contains(player)) return;
		notStalinConfirmed.add(player);
	}
	
	public List<Player> getNotStalinConfirmed() {
		return notStalinConfirmed;
	}
	
	public void addNotHitlerConfirmed(Player player) {
		if(notHitlerConfirmed.contains(player)) return;
		notHitlerConfirmed.add(player);
	}
	
	public List<Player> getNotHitlerConfirmed() {
		return notHitlerConfirmed;
	}
	
	public boolean isPlayerDead(Player player) {
		return deadPlayers.contains(player);
	}
	
	public GameRole getRole(Player p) {
		if(liberals.contains(p)) return GameRole.LIBERAL;
		if(communists.contains(p)) return GameRole.COMMUNIST;
		if(fascists.contains(p)) return GameRole.FASCIST;
		if(p.equals(hitler)) return GameRole.HITLER;
		if(p.equals(stalin)) return GameRole.STALIN;
		return null;
	}
	
	public void advancePresident() {
		presidentIndex = (presidentIndex + 1) % room.getPlayers().size();
		this.president = room.getPlayers().get(presidentIndex);
		if(isPlayerDead(president) || president.equals(blockedPlayer)) {
			advancePresident();
		}else {
			room.broadcastEventLogEntry("New president is " + president.getName());
		}
	}
	
	public void advanceRound(boolean advancePresident) {
		setMoveState(GameMoveState.SELECT_CHANCELLOR);
		if(room.getPlayers().size() >= 8) previousPresident = president;
		previousChancellor = chancellor;
		chancellor = null;
		if(advancePresident) advancePresident();
		blockedPlayer = null;
		vetoBlocked = false;
	}
	
	public void advanceRound() {
		advanceRound(true);
	}
	
	public List<GamePolicyCard> drawCards() {
		List<GamePolicyCard> subL = drawPile.subList(0, 3);
		List<GamePolicyCard> ret = new ArrayList<>(subL);
		subL.clear();
		
		if(drawPile.size() < 3) {
			drawPile.addAll(discardPile);
			discardPile.clear();
			Collections.shuffle(drawPile);
		}
		
		return ret;
	}
	
	public GamePolicyCard randomCard() {
		GamePolicyCard card = drawPile.remove(0);
		
		if(drawPile.size() < 3) {
			drawPile.addAll(discardPile);
			discardPile.clear();
			Collections.shuffle(drawPile);
		}
		
		GameBoardAction ac = null;
		switch(card) {
			case COMMUNIST:
				ac = communistBoard.addCard();
				break;
			case FASCIST:
				ac = fascistBoard.addCard();
				break;
			case LIBERAL:
				ac = liberalBoard.addCard();
				break;
		}
		
		if(ac.equals(GameBoardAction.WIN)) {
			room.setWinner(card.getParty());
			room.stopGame();
		}
		
		return card;
	}
	
	public void removePlayer(Player player) {
		deadPlayers.remove(player);
		liberals.remove(player);
		communists.remove(player);
		fascists.remove(player);
		deadPlayers.remove(player);
		notHitlerConfirmed.remove(player);
		notStalinConfirmed.remove(player);
		if(player.equals(hitler)) hitler = null;
		if(player.equals(stalin)) stalin = null;
	}
	
	@JSONValue("drawPileSize")
	@JavaScriptGetter("getDrawPileSize")
	private int getDrawPileSize() {
		return drawPile.size();
	}
	
}
