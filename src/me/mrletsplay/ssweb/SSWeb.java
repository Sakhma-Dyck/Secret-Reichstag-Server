package me.mrletsplay.ssweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.java_websocket.WebSocket;

import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.game.RoomSettings;
import me.mrletsplay.ssweb.game.state.GameMoveState;
import me.mrletsplay.ssweb.game.state.GameParty;
import me.mrletsplay.ssweb.game.state.GamePolicyCard;
import me.mrletsplay.ssweb.game.state.GameRole;
import me.mrletsplay.ssweb.game.state.GameState;
import me.mrletsplay.ssweb.game.state.board.GameBoard;
import me.mrletsplay.ssweb.game.state.board.GameBoardAction;
import me.mrletsplay.ssweb.game.state.board.GameBoardActionField;
import me.mrletsplay.ssweb.game.state.board.action.ActionBlockPlayer;
import me.mrletsplay.ssweb.game.state.board.action.ActionExamineTopCards;
import me.mrletsplay.ssweb.game.state.board.action.ActionExamineTopCardsOther;
import me.mrletsplay.ssweb.game.state.board.action.ActionInspectPlayer;
import me.mrletsplay.ssweb.game.state.board.action.ActionInspectPlayerResult;
import me.mrletsplay.ssweb.game.state.board.action.ActionKillPlayer;
import me.mrletsplay.ssweb.game.state.board.action.ActionPickPresident;
import me.mrletsplay.ssweb.packet.JavaScriptConvertible;
import me.mrletsplay.ssweb.packet.handler.PacketHandler;
import me.mrletsplay.ssweb.packet.handler.impl.ChatMessageHandler;
import me.mrletsplay.ssweb.packet.handler.impl.DiscardCardHandler;
import me.mrletsplay.ssweb.packet.handler.impl.DisconnectHandler;
import me.mrletsplay.ssweb.packet.handler.impl.DrawCardsHandler;
import me.mrletsplay.ssweb.packet.handler.impl.PerformActionHandler;
import me.mrletsplay.ssweb.packet.handler.impl.SelectChancellorHandler;
import me.mrletsplay.ssweb.packet.handler.impl.StartGameHandler;
import me.mrletsplay.ssweb.packet.handler.impl.VetoHandler;
import me.mrletsplay.ssweb.packet.handler.impl.VoteHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientChatMessage;
import me.mrletsplay.ssweb.packet.impl.PacketClientConnect;
import me.mrletsplay.ssweb.packet.impl.PacketClientDiscardCard;
import me.mrletsplay.ssweb.packet.impl.PacketClientDrawCards;
import me.mrletsplay.ssweb.packet.impl.PacketClientPerformAction;
import me.mrletsplay.ssweb.packet.impl.PacketClientSelectChancellor;
import me.mrletsplay.ssweb.packet.impl.PacketClientStartGame;
import me.mrletsplay.ssweb.packet.impl.PacketClientVeto;
import me.mrletsplay.ssweb.packet.impl.PacketClientVote;
import me.mrletsplay.ssweb.packet.impl.PacketDisconnect;
import me.mrletsplay.ssweb.packet.impl.PacketServerEventLogEntry;
import me.mrletsplay.ssweb.packet.impl.PacketServerJoinError;
import me.mrletsplay.ssweb.packet.impl.PacketServerKeepAlive;
import me.mrletsplay.ssweb.packet.impl.PacketServerNoData;
import me.mrletsplay.ssweb.packet.impl.PacketServerPickCards;
import me.mrletsplay.ssweb.packet.impl.PacketServerPlayerAction;
import me.mrletsplay.ssweb.packet.impl.PacketServerPlayerJoined;
import me.mrletsplay.ssweb.packet.impl.PacketServerPlayerLeft;
import me.mrletsplay.ssweb.packet.impl.PacketServerRoomInfo;
import me.mrletsplay.ssweb.packet.impl.PacketServerRoomList;
import me.mrletsplay.ssweb.packet.impl.PacketServerStartGame;
import me.mrletsplay.ssweb.packet.impl.PacketServerStopGame;
import me.mrletsplay.ssweb.packet.impl.PacketServerUpdateGameState;
import me.mrletsplay.ssweb.packet.impl.PacketServerVeto;
import me.mrletsplay.ssweb.packet.impl.PacketServerVoteResults;

public class SSWeb {
	
	public static final boolean IS_BETA = false;
	
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
				.filter(p -> p.getWebSocket().equals(socket))
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
