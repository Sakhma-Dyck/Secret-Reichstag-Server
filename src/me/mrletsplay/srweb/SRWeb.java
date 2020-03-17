package me.mrletsplay.srweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.java_websocket.WebSocket;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.RoomSettings;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.game.state.GameParty;
import me.mrletsplay.srweb.game.state.GamePolicyCard;
import me.mrletsplay.srweb.game.state.GameRole;
import me.mrletsplay.srweb.game.state.GameState;
import me.mrletsplay.srweb.game.state.board.GameBoard;
import me.mrletsplay.srweb.game.state.board.GameBoardAction;
import me.mrletsplay.srweb.game.state.board.GameBoardActionField;
import me.mrletsplay.srweb.game.state.board.action.ActionBlockPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionExamineTopCards;
import me.mrletsplay.srweb.game.state.board.action.ActionExamineTopCardsOther;
import me.mrletsplay.srweb.game.state.board.action.ActionInspectPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionInspectPlayerResult;
import me.mrletsplay.srweb.game.state.board.action.ActionKillPlayer;
import me.mrletsplay.srweb.game.state.board.action.ActionPickPresident;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.handler.PacketHandler;
import me.mrletsplay.srweb.packet.handler.impl.ChatMessageHandler;
import me.mrletsplay.srweb.packet.handler.impl.DiscardCardHandler;
import me.mrletsplay.srweb.packet.handler.impl.DisconnectHandler;
import me.mrletsplay.srweb.packet.handler.impl.DrawCardsHandler;
import me.mrletsplay.srweb.packet.handler.impl.PerformActionHandler;
import me.mrletsplay.srweb.packet.handler.impl.SelectChancellorHandler;
import me.mrletsplay.srweb.packet.handler.impl.StartGameHandler;
import me.mrletsplay.srweb.packet.handler.impl.VetoHandler;
import me.mrletsplay.srweb.packet.handler.impl.VoteHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientChatMessage;
import me.mrletsplay.srweb.packet.impl.PacketClientConnect;
import me.mrletsplay.srweb.packet.impl.PacketClientDiscardCard;
import me.mrletsplay.srweb.packet.impl.PacketClientDrawCards;
import me.mrletsplay.srweb.packet.impl.PacketClientPerformAction;
import me.mrletsplay.srweb.packet.impl.PacketClientSelectChancellor;
import me.mrletsplay.srweb.packet.impl.PacketClientStartGame;
import me.mrletsplay.srweb.packet.impl.PacketClientVeto;
import me.mrletsplay.srweb.packet.impl.PacketClientVote;
import me.mrletsplay.srweb.packet.impl.PacketDisconnect;
import me.mrletsplay.srweb.packet.impl.PacketServerEventLogEntry;
import me.mrletsplay.srweb.packet.impl.PacketServerJoinError;
import me.mrletsplay.srweb.packet.impl.PacketServerKeepAlive;
import me.mrletsplay.srweb.packet.impl.PacketServerNoData;
import me.mrletsplay.srweb.packet.impl.PacketServerPauseGame;
import me.mrletsplay.srweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerAction;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerJoined;
import me.mrletsplay.srweb.packet.impl.PacketServerPlayerLeft;
import me.mrletsplay.srweb.packet.impl.PacketServerRoomInfo;
import me.mrletsplay.srweb.packet.impl.PacketServerRoomList;
import me.mrletsplay.srweb.packet.impl.PacketServerStartGame;
import me.mrletsplay.srweb.packet.impl.PacketServerStopGame;
import me.mrletsplay.srweb.packet.impl.PacketServerUnpauseGame;
import me.mrletsplay.srweb.packet.impl.PacketServerUpdateGameState;
import me.mrletsplay.srweb.packet.impl.PacketServerVeto;
import me.mrletsplay.srweb.packet.impl.PacketServerVoteResults;

public class SRWeb {
	
	/*
	 * TODO REJOIN
	 * - Add/remove start btn accordingly
	 * - Add un-/pause functionality to client
	 * - Display player as offline
	 */
	
	public static final List<Class<? extends JavaScriptConvertible>> SERIALIZABLE_CLASSES = Arrays.asList(
				PacketClientConnect.class,
				PacketClientDrawCards.class,
				PacketClientDiscardCard.class,
				PacketClientVote.class,
				PacketClientSelectChancellor.class,
				PacketClientStartGame.class,
				PacketClientPerformAction.class,
				PacketClientVeto.class,
				PacketClientChatMessage.class,
				
				PacketDisconnect.class,
				PacketServerRoomList.class,
				PacketServerRoomInfo.class,
				PacketServerJoinError.class,
				PacketServerPlayerJoined.class,
				PacketServerUpdateGameState.class,
				PacketServerPlayerAction.class,
				PacketServerPickCards.class,
				PacketServerNoData.class,
				PacketServerVoteResults.class,
				PacketServerPlayerLeft.class,
				PacketServerStartGame.class,
				PacketServerStopGame.class,
				PacketServerVeto.class,
				PacketServerEventLogEntry.class,
				PacketServerKeepAlive.class,
				PacketServerPauseGame.class,
				PacketServerUnpauseGame.class,
				
				Room.class,
				Player.class,
				GameParty.class,
				GamePolicyCard.class,
				GameState.class,
				GameBoard.class,
				GameBoardAction.class,
				GameBoardActionField.class,
				GameMoveState.class,
				GameRole.class,
				RoomSettings.class,
				ActionKillPlayer.class,
				ActionExamineTopCards.class,
				ActionInspectPlayer.class,
				ActionInspectPlayerResult.class,
				ActionExamineTopCardsOther.class,
				ActionPickPresident.class,
				ActionBlockPlayer.class
			);
	
	public static final List<PacketHandler> PACKET_HANDLERS = Arrays.asList(
				new DisconnectHandler(),
				new DrawCardsHandler(),
				new DiscardCardHandler(),
				new VoteHandler(),
				new SelectChancellorHandler(),
				new StartGameHandler(),
				new PerformActionHandler(),
				new VetoHandler(),
				new ChatMessageHandler()
			);
	
	private static List<Player> players = new ArrayList<>();
	private static List<Room> rooms = new ArrayList<>();
	
	public static void addPlayer(Player player) {
		players.add(player);
	}
	
	public static void removePlayer(Player player) {
		players.remove(player);
		new ArrayList<>(rooms).forEach(r -> {
			if(r.getPlayers().contains(player)) r.removePlayer(player);
		});
	}
	
	public static Player getPlayer(WebSocket socket) {
		return players.stream()
				.filter(p -> p.getWebSocket() != null && p.getWebSocket().equals(socket))
				.findFirst().orElse(null);
	}
	
	public static List<Player> getPlayers() {
		return players;
	}
	
	public static Room createRoom(String name, RoomSettings settings) {
		Room r = new Room(name, settings);
		rooms.add(r);
		return r;
	}
	
	public static void removeRoom(Room room) {
		rooms.remove(room);
	}
	
	public static Room getRoom(String id) {
		return rooms.stream()
				.filter(r -> r.getID().equals(id))
				.findFirst().orElse(null);
	}
	
	public static List<Room> getRooms() {
		return rooms;
	}
	
}
