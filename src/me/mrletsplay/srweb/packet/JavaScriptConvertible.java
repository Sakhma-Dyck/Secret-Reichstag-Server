package me.mrletsplay.srweb.packet;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;

public interface JavaScriptConvertible extends JSONConvertible {
	
	@Override
	public default void preSerialize(JSONObject object) {
		object.put("jsClass", getClass().getSimpleName());
	}

}
