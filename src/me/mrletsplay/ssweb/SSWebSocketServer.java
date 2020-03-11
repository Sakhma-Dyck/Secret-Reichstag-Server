package me.mrletsplay.ssweb;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.ssweb.game.Player;
import me.mrletsplay.ssweb.game.Room;
import me.mrletsplay.ssweb.packet.ClassSerializer;
import me.mrletsplay.ssweb.packet.JavaScriptConvertible;
import me.mrletsplay.ssweb.packet.Packet;
import me.mrletsplay.ssweb.packet.handler.PacketHandler;
import me.mrletsplay.ssweb.packet.impl.PacketClientConnect;
import me.mrletsplay.ssweb.packet.impl.PacketServerJoinError;
import me.mrletsplay.ssweb.packet.impl.PacketServerKeepAlive;
import me.mrletsplay.ssweb.packet.impl.PacketServerRoomInfo;

public class SSWebSocketServer extends WebSocketServer {
	
	private List<PacketHandler> handlers;
	
	public SSWebSocketServer(InetSocketAddress address) {
		super(address);
		this.handlers = new ArrayList<>();
		setReuseAddr(true);
		setTcpNoDelay(true);
		
		if(!SSWeb.IS_BETA) {
			SSLContext ctx = SSLStuff.getContext();
			System.out.println("SSL ctx: " + ctx);
			setWebSocketFactory(new DefaultSSLWebSocketServerFactory(ctx));
		}
		
		new Thread(() -> {
			while(true) {
				for(Player p : SSWeb.getPlayers()) {
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
		System.out.println("CONNECT: " + conn.getRemoteSocketAddress());
		JSONObject obj = new JSONObject();
		obj.put("init", true);
		
		JSONArray array = new JSONArray();
		for(Class<? extends JavaScriptConvertible> cls : SSWeb.SERIALIZABLE_CLASSES) {
			array.add(ClassSerializer.serializeClass(cls));
		}
		obj.put("classes", array);
		
		conn.send(obj.toFancyString());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("DISCONNECT: " + conn.getRemoteSocketAddress() + " (" + code + ")");
		Player p = SSWeb.getPlayer(conn);
		if(p != null) SSWeb.removePlayer(p);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if(message.length() > 16384) {
			conn.close(CloseFrame.TOOBIG);
			return;
		}
		
		System.out.println(">> " + message);
		
		Packet p = JSONConverter.decodeObject(new JSONObject(message), Packet.class);
		Player pl = SSWeb.getPlayer(conn);
		
		if(pl == null) {
			if(p.getData() instanceof PacketClientConnect) {
				PacketClientConnect con = (PacketClientConnect) p.getData();
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
					
					r = SSWeb.createRoom(con.getRoomName(), con.getRoomSettings());
				}else {
					if(con.getRoomID() == null || con.getRoomID().isEmpty()) {
						conn.close(CloseFrame.POLICY_VALIDATION);
						return;
					}
					
					r = SSWeb.getRoom(con.getRoomID());
					if(r == null) {
						pl.send(new Packet(p.getID(), new PacketServerJoinError("Invalid room id")));
						conn.close();
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

				SSWeb.addPlayer(pl);
				r.addPlayer(pl);
				pl.send(new Packet(p.getID(), new PacketServerRoomInfo(pl, r)));
				return;
			}else {
				conn.close(CloseFrame.POLICY_VALIDATION, "Not a connect packet");
				return;
			}
		}
		
		for(PacketHandler handler : handlers) {
			if(handler.shouldHandle(p)) {
				try {
					SSWebContext.setCurrentPlayer(pl);
					pl.send(new Packet(p.getID(), handler.handle(pl, p, p.getData())));
				}catch(Exception e) {
					e.printStackTrace();
					conn.close(CloseFrame.POLICY_VALIDATION, "Exception in handler");
				}
				return;
			}
		}
		
		conn.close(CloseFrame.POLICY_VALIDATION, "No handler available");
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onStart() {
		System.out.println("SERVER IS ON: " + getPort());
	}
	
	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft,
			ClientHandshake request) throws InvalidDataException {
		System.out.println("HS RECEIVED");
		return super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
	}
	
	@Override
	protected boolean onConnect(SelectionKey key) {
		boolean b = super.onConnect(key);
		System.out.println("ONCONNECT: " + b);
		return b;
	}
	
}
