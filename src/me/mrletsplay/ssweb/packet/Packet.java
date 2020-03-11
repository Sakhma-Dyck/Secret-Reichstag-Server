package me.mrletsplay.ssweb.packet;

import java.util.UUID;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Packet implements JSONConvertible {
	
	@JSONValue
	private String id;
	
	@JSONValue
	private String referrerID;
	
	@JSONValue
	private boolean success;
	
	@JSONValue
	private PacketData data;
	
	@JSONValue
	private String errorMessage;
	
	@JSONConstructor
	private Packet() {}

	public Packet(String id, String referrerID, boolean success, PacketData data, String errorMessage) {
		this.id = id;
		this.referrerID = referrerID;
		this.success = success;
		this.data = data;
		this.errorMessage = errorMessage;
	}
	
	public Packet(String referrerID, PacketData data) {
		this(randomID(), referrerID, true, data, null);
	}
	
	public Packet(String referrerID, String errorMessage) {
		this(randomID(), referrerID, false, null, errorMessage);
	}
	
	public Packet(PacketData data) {
		this(randomID(), null, true, data, null);
	}
	
	public String getID() {
		return id;
	}
	
	public String getReferrerID() {
		return referrerID;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public PacketData getData() {
		return data;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	private static String randomID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
