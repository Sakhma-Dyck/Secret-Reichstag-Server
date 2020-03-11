package me.mrletsplay.ssweb.game;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.ssweb.game.state.GamePolicyCard;
import me.mrletsplay.ssweb.packet.JavaScriptConvertible;
import me.mrletsplay.ssweb.packet.JavaScriptGetter;
import me.mrletsplay.ssweb.packet.Packet;

public class Player implements JavaScriptConvertible {

	private WebSocket webSocket;
	
	private Room room;
	
	@JSONValue
	@JavaScriptGetter("getID")
	private String id;
	
	@JSONValue
	@JavaScriptGetter("getName")
	private String name;
	
	private List<GamePolicyCard> hand;
	
	public Player(WebSocket webSocket, String name) {
		this.webSocket = webSocket;
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.hand = Collections.emptyList();
	}
	
	public WebSocket getWebSocket() {
		return webSocket;
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setHand(List<GamePolicyCard> hand) {
		this.hand = hand;
	}
	
	public List<GamePolicyCard> getHand() {
		return hand;
	}
	
	public void send(Packet p) {
		webSocket.send(p.toJSON().toString());
	}
	
}
