package me.mrletsplay.srweb.packet;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.misc.FriendlyException;

public interface JavaScriptEnum extends JavaScriptConvertible {
	
	@Override
	public default void preSerialize(JSONObject object) {
		if(!(this instanceof Enum<?>)) throw new FriendlyException("Not an enum");
		JavaScriptConvertible.super.preSerialize(object);
		object.put("jsEnumName", ((Enum<?>) this).name());
	}

}
