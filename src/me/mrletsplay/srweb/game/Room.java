package me.mrletsplay.srweb.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.srweb.SRWeb;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.game.state.GameRole;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.JavaScriptGetter;
import me.mrletsplay.srweb.packet.JavaScriptSetter;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.impl.PacketServerEventLogEntry;
import me.mrletsplay.srweb.packet.impl.PacketServerPauseGame;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerAction;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerJoined;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerLeft;
import me.mrletsplay.srweb.packet.impl.PacketServerStartGame;
import me.mrletsplay.srweb.packet.impl.PacketServerStopGame;
import me.mrletsplay.srweb.packet.impl.PacketServerUnpauseGame;
import me.mrletsplay.srweb.packet.impl.PacketServerUpdateGameState;
import me.mrletsplay.srweb.packet.impl.PacketServerVeto;

public class Room implements JavaScriptConvertible {
	
	private Random random;
	
	@JSONValue
	@JavaScriptGetter("getID")
	private String id;
	
	@JSONValue
	@JavaScriptGetter("getName")
	private String name;
	
	@JSONValue
	@JavaScriptGetter("getMode")
	private GameMode mode;
	
	@JSONValue
	@JavaScriptGetter("getPlayers")
	private List<Player> players;
	
	@JSONValue
	@JavaScriptGetter("getGameState")
	@JavaScriptSetter("setGameState")
	private GameState gameState;
	
	@JSONValue
	@JavaScriptGetter("isGameRunning")
	@JavaScriptSetter("setGameRunning")
	private boolean gameRunning;
	
	@JSONValue
	@JavaScriptGetter("isGamePaused")
	@JavaScriptSetter("setGamePaused")
	private boolean gamePaused;
	
	@JSONValue
	@JavaScriptGetter("getSettings")
	private RoomSettings settings;
	
	@JSONValue
	@JavaScriptGetter("getWinner")
	private GameParty winner;
	
	public Room(String name, GameMode mode, RoomSettings settings) {
		this.random = new Random();
		this.id = randomID();
		this.name = name;
		this.mode = mode;
		this.players = new ArrayList<>();
		this.settings = settings;
		this.gameState = new GameState(this);
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public GameMode getMode() {
		return mode;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public boolean isGameRunning() {
		return gameRunning;
	}
	
	public boolean isGamePaused() {
		return gamePaused;
	}
	
	public void setSettings(RoomSettings settings) {
		this.settings = settings;
	}
	
	public RoomSettings getSettings() {
		return settings;
	}
	
	public void setWinner(GameParty winner) {
		this.winner = winner;
	}
	
	public GameParty getWinner() {
		return winner;
	}
	
	public void addPlayer(Player player) {
		player.setRoom(this);
		players.forEach(p -> p.send(new Packet(new PacketServerPlayerJoined(player, false))));
		players.add(player);
		
		broadcastEventLogEntry(player.getName() + " joined");
	}
	
	public void rejoinPlayer(Player player) {
		players.forEach(p -> p.send(new Packet(new PacketServerPlayerJoined(player, true))));
		
		player.send(new Packet(getStartPackage(gameState.getRole(player))));
		
		if(players.stream().allMatch(Player::isOnline)) unpauseGame();
		
		// Tell player everything they need to know to continue
		
		if((gameState.getMoveState().equals(GameMoveState.DISCARD_PRESIDENT) && player.equals(gameState.getPresident()))
				|| (gameState.getMoveState().equals(GameMoveState.DISCARD_CHANCELLOR) && player.equals(gameState.getChancellor()))) {
			player.send(new Packet(new PacketServerPickCards(player.getHand(), gameState.isVetoBlocked())));
		}
		
		if(gameState.getMoveState().equals(GameMoveState.DISCARD_CHANCELLOR) && player.equals(gameState.getPresident()) && gameState.isVetoPowerUnlocked() && gameState.isVetoRequested()) {
			player.send(new Packet(new PacketServerVeto()));
		}
		
		if(gameState.getMoveState().equals(GameMoveState.ACTION) && player.equals(gameState.getActionPerformer())) {
			player.send(new Packet(new PacketServerPlayerAction(gameState.getAction(), player.getActionData())));
		}
		
		broadcastEventLogEntry(player.getName() + " rejoined");
	}
	
	public void removePlayer(Player player) {
		if(!players.contains(player)) return;
		
		boolean closeRoom = players.isEmpty() || players.stream().allMatch(p -> !p.isOnline());
		boolean hardLeave = closeRoom || !gameRunning /*|| gameState.isPlayerDead(player)*/;
		
		if(hardLeave) {
			player.setRoom(null);
			gameState.getDeadPlayers().remove(player);
			players.remove(player);
		}
		
		if(closeRoom) SRWeb.removeRoom(this);
		players.forEach(p -> p.send(new Packet(new PacketServerPlayerLeft(player, hardLeave))));
		if(gameRunning && !gameState.isPlayerDead(player)) {
			pauseGame();
		}
		
		broadcastEventLogEntry(player.getName() + " left");
	}
	
	public void startGame() {
		if(gameRunning) stopGame();
		gameRunning = true;
		
		int nL = 0, nCF = 0;
		
		int r = players.size() % 3;
		if(r == 0) {
			nL = players.size() / 3 + 2;
			nCF = players.size() / 3 - 1;
		} else if(r == 1) {
			nL = (players.size() - 1) / 3 + 1;
			nCF = (players.size() - 1) / 3;
		} else if(r == 2) {
			nL = (players.size() - 2) / 3 + 2;
			nCF = (players.size() - 2) / 3;
		}
		
		if(players.size() == 2) { // NONBETA: debug
			nL = 0;
			nCF = 1;
		}
		
		List<Player> remainingPlayers = new ArrayList<>(players);
		List<Player> liberals = new ArrayList<>();
		List<Player> communists = new ArrayList<>();
		List<Player> fascists = new ArrayList<>();
		
		for(int i = 0; i < nL; i++) {
			liberals.add(remainingPlayers.remove(random.nextInt(remainingPlayers.size())));
		}
		
		for(int i = 0; i < nCF; i++) {
			communists.add(remainingPlayers.remove(random.nextInt(remainingPlayers.size())));
		}
		
		Player stalin = communists.remove(random.nextInt(communists.size()));
		
		for(int i = 0; i < nCF; i++) {
			fascists.add(remainingPlayers.remove(random.nextInt(remainingPlayers.size())));
		}
		
		Player hitler = fascists.remove(random.nextInt(fascists.size()));
		
		gameState.setMoveState(GameMoveState.SELECT_CHANCELLOR);
		gameState.setLiberals(liberals);
		gameState.setStalin(stalin);
		gameState.setCommunists(communists);
		gameState.setHitler(hitler);
		gameState.setFascists(fascists);
		
		int presidentIndex = random.nextInt(players.size());
		gameState.setPresident(players.get(presidentIndex));
		gameState.setPresidentIndex(presidentIndex);
		
		// shows players what they're supposed to see
		liberals.forEach(l -> l.send(new Packet(getStartPackage(GameRole.LIBERAL))));
		
		stalin.send(new Packet(getStartPackage(GameRole.STALIN)));
		communists.forEach(l -> l.send(new Packet(getStartPackage(GameRole.COMMUNIST))));
		
		hitler.send(new Packet(getStartPackage(GameRole.HITLER)));
		fascists.forEach(l -> l.send(new Packet(getStartPackage(GameRole.FASCIST))));
		
		broadcastStateUpdate();
	}
	
	private PacketServerStartGame getStartPackage(GameRole role) {
		switch(role) {
			case LIBERAL:
				return new PacketServerStartGame(GameRole.LIBERAL, null, null);
			case STALIN:
				return new PacketServerStartGame(GameRole.STALIN, null, players.size() > 9 ? null : gameState.getCommunists());
			case COMMUNIST:
				return new PacketServerStartGame(GameRole.COMMUNIST, gameState.getStalin(), gameState.getCommunists());
			case HITLER:
				return new PacketServerStartGame(GameRole.HITLER, null, players.size() > 9 ? null : gameState.getFascists());
			case FASCIST:
				return new PacketServerStartGame(GameRole.FASCIST, gameState.getHitler(), gameState.getFascists());
			default:
				return null;
		}
	}
	
	public Player getPlayer(String id) {
		return players.stream()
				.filter(p -> p.getID().equals(id))
				.findFirst().orElse(null);
	}
	
	public boolean isFull() {
		return players.size() >= settings.getPlayerCount();
	}
	
	public void pauseGame() {
		gamePaused = true;
		players.forEach(p -> p.send(new Packet(new PacketServerPauseGame())));
		broadcastStateUpdate();
	}
	
	public void unpauseGame() {
		gamePaused = false;
		players.forEach(p -> p.send(new Packet(new PacketServerUnpauseGame())));
		broadcastStateUpdate();
	}
	
	public void stopGame() {
		if(!gameRunning) return;
		gameRunning = false;
		
		Map<String, GameRole> roles = new HashMap<>();
		
		for(Player player : players) {
			roles.put(player.getID(), gameState.getRole(player));
		}
		
		gameState = new GameState(this);
		
		players.forEach(p -> p.send(new Packet(new PacketServerStopGame(winner, roles))));		
		if(winner != null) {
			broadcastEventLogEntry(winner.getFriendlyName() + " win");
		}else {
			broadcastEventLogEntry("Game stopped");
		}
		
		winner = null;
		
		broadcastStateUpdate();
	}
	
	public void broadcastStateUpdate() {
		players.forEach(p -> p.send(new Packet(new PacketServerUpdateGameState(gameState))));
	}
	
	public void broadcastEventLogEntry(String message, boolean isChatMessage) {
		players.forEach(p -> p.send(new Packet(new PacketServerEventLogEntry(message, isChatMessage))));
	}
	
	public void broadcastEventLogEntry(String message) {
		broadcastEventLogEntry(message, false);
	}
	
	private String randomID() {
		while(isIDTaken(id = genID()));
		return id;
	}
	
	private boolean isIDTaken(String id) {
		return SRWeb.getRooms().stream().anyMatch(rm -> rm.getID().equals(id));
	}
	
	private String genID() {
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < 6; i++) {
			b.append(random.nextInt(10));
		}
		return b.toString();
	}
	
}
