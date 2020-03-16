package me.mrletsplay.srweb.game;

import java.util.ArrayList;
import java.util.List;
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
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerJoined;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerLeft;
import me.mrletsplay.srweb.packet.impl.PacketServerStartGame;
import me.mrletsplay.srweb.packet.impl.PacketServerStopGame;
import me.mrletsplay.srweb.packet.impl.PacketServerUpdateGameState;

public class Room implements JavaScriptConvertible {
	
	private Random random;
	
	@JSONValue
	@JavaScriptGetter("getID")
	private String id;
	
	@JSONValue
	@JavaScriptGetter("getName")
	private String name;
	
	@JSONValue
	@JavaScriptGetter("getPlayers")
	private List<Player> players;
	
	@JSONValue
	@JavaScriptGetter("getGameState")
	@JavaScriptSetter("setGameState")
	private GameState gameState;
	
	@JSONValue
	@JavaScriptGetter("isGameRunning")
	private boolean gameRunning;
	
	@JSONValue
	@JavaScriptGetter("getSettings")
	private RoomSettings settings;
	
	@JSONValue
	@JavaScriptGetter("getWinner")
	private GameParty winner;
	
	public Room(String name, RoomSettings settings) {
		this.random = new Random();
		this.id = randomID();
		this.name = name;
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
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public boolean isGameRunning() {
		return gameRunning;
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
		players.forEach(p -> p.send(new Packet(new PacketServerPlayerJoined(player))));
		players.add(player);
		
		broadcastEventLogEntry(player.getName() + " joined");
	}
	
	public void removePlayer(Player player) {
		if(!players.remove(player)) return;
		player.setRoom(null);
		if(players.isEmpty()) SRWeb.removeRoom(this);
		players.forEach(p -> p.send(new Packet(new PacketServerPlayerLeft(player))));
		if(gameRunning && !gameState.isPlayerDead(player)) stopGame();
		gameState.getDeadPlayers().remove(player);
		
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
		liberals.forEach(l -> l.send(new Packet(new PacketServerStartGame(GameRole.LIBERAL, null, null))));
		
		stalin.send(new Packet(new PacketServerStartGame(GameRole.STALIN, null, players.size() > 9 ? null : communists)));
		communists.forEach(l -> l.send(new Packet(new PacketServerStartGame(GameRole.COMMUNIST, stalin, communists))));
		
		hitler.send(new Packet(new PacketServerStartGame(GameRole.HITLER, null, players.size() > 9 ? null : fascists)));
		fascists.forEach(l -> l.send(new Packet(new PacketServerStartGame(GameRole.FASCIST, hitler, fascists))));
		
		broadcastStateUpdate();
	}
	
	public Player getPlayer(String id) {
		return players.stream()
				.filter(p -> p.getID().equals(id))
				.findFirst().orElse(null);
	}
	
	public boolean isFull() {
		return players.size() >= settings.getPlayerCount();
	}
	
	public void stopGame() {
		if(!gameRunning) return;
		gameRunning = false;
		gameState = new GameState(this);
		
		players.forEach(p -> p.send(new Packet(new PacketServerStopGame(winner))));		
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
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < 4; i++) { // NONBETA: longer ids
			b.append(random.nextInt(10));
		}
		String id = b.toString();
		if(SRWeb.getRooms().stream().anyMatch(rm -> rm.getID().equals(id))) return randomID();
		return id;
	}
	
}
