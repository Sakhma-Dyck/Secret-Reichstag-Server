package me.mrletsplay.srweb.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.srweb.SRWeb;
import me.mrletsplay.srweb.game.GameMode;
import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.game.Room;
import me.mrletsplay.srweb.game.state.GameMoveState;
import me.mrletsplay.srweb.packet.ClassSerializer;
import me.mrletsplay.srweb.packet.JavaScriptConvertible;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.packet.handler.PacketHandler;
import me.mrletsplay.srweb.packet.impl.PacketClientConnect;
import me.mrletsplay.srweb.packet.impl.PacketServerJoinError;
import me.mrletsplay.srweb.packet.impl.PacketServerKeepAlive;
import me.mrletsplay.srweb.packet.impl.PacketServerRoomInfo;
import me.mrletsplay.srweb.session.PlayerSession;
import me.mrletsplay.srweb.session.SRWebSessionStore;

public class SRWebSocketServer extends WebSocketServer {
	
	private static final Pattern NAME_PATTERN = Pattern.compile("(?:[a-zA-Z0-9äöü ]){1,20}");
	
	private List<PacketHandler> handlers;
	
	public SRWebSocketServer(InetSocketAddress address, boolean enableSSL) {
		super(address);
		this.handlers = new ArrayList<>();
		setReuseAddr(true);
		setTcpNoDelay(true);
		
		if(enableSSL) {
			SSLContext ctx = SSLHelper.getContext();
			setWebSocketFactory(new DefaultSSLWebSocketServerFactory(ctx));
			System.out.println("Successfully enabled SSL");
		}
		
		new Thread(() -> {
			while(true) {
				for(Player p : SRWeb.getPlayers()) {
					try {
						p.send(new Packet(new PacketServerKeepAlive()));
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}, "Server-KeepAlive").start();
	}
	
	public void addHandler(PacketHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("Connection from " + conn.getRemoteSocketAddress());
		JSONObject obj = new JSONObject();
		obj.put("init", true);
		
		JSONArray array = new JSONArray();
		for(Class<? extends JavaScriptConvertible> cls : SRWeb.SERIALIZABLE_CLASSES) {
			array.add(ClassSerializer.serializeClass(cls));
		}
		obj.put("classes", array);
		
		conn.send(obj.toFancyString());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("Disconnect from " + conn.getRemoteSocketAddress() + " (code: " + code + ", remote: " + remote + ", reason: " + reason + ")");
		Player p = SRWeb.getPlayer(conn);
		if(p != null) {
			p.setWebSocket(null);
			SRWeb.removePlayer(p);
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if(message.length() > 16384) {
			conn.close(CloseFrame.TOOBIG);
			return;
		}
		
		System.out.println(">> " + message);
		
		Packet p = JSONConverter.decodeObject(new JSONObject(message), Packet.class);
		Player pl = SRWeb.getPlayer(conn);
		
		if(pl == null) {
			if(p.getData() instanceof PacketClientConnect) {
				try {
					PacketClientConnect con = (PacketClientConnect) p.getData();
					
					if(con.getSessionID() != null) {
						PlayerSession sess = SRWebSessionStore.getSession(con.getSessionID());
						
						if(sess == null) {
							conn.send(new Packet(p.getID(), new PacketServerJoinError("Cannot rejoin, session expired")).toJSON().toString());
							return;
						}
						
						pl = sess.getPlayer();
						
						if(pl.isOnline()) {
							WebSocket oldWebSocket = pl.getWebSocket();
							pl.setWebSocket(conn);
							pl.send(new Packet(p.getID(), new PacketServerRoomInfo(con.getSessionID(), pl, pl.getRoom())));
							if(oldWebSocket != null) oldWebSocket.close(CloseFrame.NORMAL, "Connected from another location"); // Kick the old player
							return;
						}
						
						if(pl.getRoom() == null) {
							conn.send(new Packet(p.getID(), new PacketServerJoinError("Cannot rejoin, player left normally")).toJSON().toString());
							return;
						}
						
						pl.setWebSocket(conn);
						
						if(SRWeb.getRoom(pl.getRoom().getID()) == null) {
							conn.send(new Packet(p.getID(), new PacketServerJoinError("Cannot rejoin, room closed")).toJSON().toString());
							return;
						}
						
						SRWeb.addPlayer(pl);
						PacketServerRoomInfo rI = new PacketServerRoomInfo(con.getSessionID(), pl, pl.getRoom());
						
						if(pl.getRoom().isGameRunning() && pl.getRoom().getGameState().getMoveState().equals(GameMoveState.VOTE)
								&& pl.getRoom().getGameState().getVoteState().getVote(pl) != null) {
							rI.setVoteDone(true);
						}
						
						pl.send(new Packet(p.getID(), rI));
						pl.getRoom().rejoinPlayer(pl);
						return;
					}
					
					String pName = con.getPlayerName().trim();
					
					if(pName.isEmpty()) {
						conn.send(new Packet(p.getID(), new PacketServerJoinError("Name cannot be empty")).toJSON().toString());
						return;
					}
					
					if(pName.length() > 20) {
						conn.send(new Packet(p.getID(), new PacketServerJoinError("Name cannot be longer than 20 characters")).toJSON().toString());
						return;
					}
					
					Matcher m = NAME_PATTERN.matcher(pName);
					if(!m.matches()) {
						conn.send(new Packet(p.getID(), new PacketServerJoinError("Name contains invalid characters")).toJSON().toString());
						return;
					}
					
					pl = new Player(conn, con.getPlayerName());
					
					Room r;
					
					if(con.isCreateRoom()) {
						if(con.getRoomName() == null || con.getRoomName().isEmpty()) {
							conn.close(CloseFrame.POLICY_VALIDATION);
							return;
						}
						
						if(!con.getRoomSettings().isValid()) {
							pl.send(new Packet(p.getID(), new PacketServerJoinError("Invalid room settings")));
							return;
						}
						
						r = SRWeb.createRoom(con.getRoomName(), GameMode.valueOf(con.getRoomSettings().getMode()), con.getRoomSettings());
					}else {
						if(con.getRoomID() == null || con.getRoomID().isEmpty()) {
							conn.close(CloseFrame.POLICY_VALIDATION);
							return;
						}
						
						r = SRWeb.getRoom(con.getRoomID());
						if(r == null) {
							pl.send(new Packet(p.getID(), new PacketServerJoinError("Invalid room id")));
							conn.close();
							return;
						}
						
						if(r.getPlayers().stream().anyMatch(o -> o.getName().toLowerCase().equals(pName.toLowerCase()))) {
							conn.send(new Packet(p.getID(), new PacketServerJoinError("Name already taken")).toJSON().toString());
							return;
						}
						
						if(r.isFull()) {
							pl.send(new Packet(p.getID(), new PacketServerJoinError("Room is full")));
							conn.close();
							return;
						}
						
						if(r.isGameRunning()) {
							pl.send(new Packet(p.getID(), new PacketServerJoinError("Game is in progress")));
							conn.close();
							return;
						}
					}
	
					SRWeb.addPlayer(pl);
					String sessID = SRWebSessionStore.createSession(pl);
					r.addPlayer(pl);
					pl.send(new Packet(p.getID(), new PacketServerRoomInfo(sessID, pl, r)));
					return;
				}catch(Exception e) {
					e.printStackTrace();
					conn.close(CloseFrame.POLICY_VALIDATION, "Invalid connect request");
					return;
				}
			}else {
				conn.close(CloseFrame.POLICY_VALIDATION, "Not a connect packet");
				return;
			}
		}
		
		handlePacket(pl, p);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onStart() {
		System.out.println("Server is listening on port " + getPort());
	}
	
	public void handlePacket(Player player, Packet packet) {
		for(PacketHandler handler : handlers) {
			if(handler.shouldHandle(packet)) {
				try {
					SRWebContext.setCurrentPlayer(player);
					player.send(new Packet(packet.getID(), handler.handle(player, packet, packet.getData())));
				}catch(Exception e) {
					e.printStackTrace();
					if(player.getWebSocket() != null) player.getWebSocket().close(CloseFrame.POLICY_VALIDATION, "Exception in handler");
				}
				return;
			}
		}

		
		if(player.getWebSocket() != null) player.getWebSocket().close(CloseFrame.POLICY_VALIDATION, "No handler available");
	}
	
}
