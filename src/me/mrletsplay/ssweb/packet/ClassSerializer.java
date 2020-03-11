package me.mrletsplay.ssweb.packet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONValue;
import me.mrletsplay.mrcore.misc.ClassUtils;

public class ClassSerializer {
	
	public static JSONObject serializeClass(Class<? extends JavaScriptConvertible> packetClass) {
		JSONObject obj = new JSONObject();
		obj.put("name", packetClass.getSimpleName());
		obj.put("javaName", packetClass.getName());
		
		JSONArray instanceMethods = new JSONArray();
		for(Field f : ClassUtils.getFields(packetClass)) {
			JSONValue val = f.getAnnotation(JSONValue.class);
			if(val == null || !val.encode()) continue;
			String fName = val.value().isEmpty() ? f.getName() : val.value();
			
			JavaScriptGetter get = f.getAnnotation(JavaScriptGetter.class);
			
			if(get != null) {
				JSONObject getter = new JSONObject();
				getter.put("type", "getter");
				getter.put("name", get.value());
				getter.put("field", fName);
				instanceMethods.add(getter);
			}
			
			JavaScriptSetter set = f.getAnnotation(JavaScriptSetter.class);
			
			if(set != null) {
				JSONObject setter = new JSONObject();
				setter.put("type", "setter");
				setter.put("name", set.value());
				setter.put("field", fName);
				instanceMethods.add(setter);
			}
		}
		
		for(Method m : ClassUtils.getMethods(packetClass)) {
			JSONValue val = m.getAnnotation(JSONValue.class);
			if(val == null || !val.encode()) continue;
			String fName = val.value().isEmpty() ? m.getName() : val.value();
			
			JavaScriptGetter get = m.getAnnotation(JavaScriptGetter.class);
			
			if(get != null) {
				JSONObject getter = new JSONObject();
				getter.put("type", "getter");
				getter.put("name", get.value());
				getter.put("field", fName);
				instanceMethods.add(getter);
			}
			
			JavaScriptSetter set = m.getAnnotation(JavaScriptSetter.class);
			
			if(set != null) {
				JSONObject setter = new JSONObject();
				setter.put("type", "setter");
				setter.put("name", set.value());
				setter.put("field", fName);
				instanceMethods.add(setter);
			}
		}
		
		obj.put("instanceMethods", instanceMethods);
		
		if(JavaScriptEnum.class.isAssignableFrom(packetClass) && Enum.class.isAssignableFrom(packetClass)) {
			JSONObject enumValues = new JSONObject();
			
			for(JavaScriptConvertible e : packetClass.getEnumConstants()) {
				enumValues.put(((Enum<?>) e).name(), e.toJSON());
			}
			
			obj.put("isEnum", true);
			obj.put("enumValues", enumValues);
		}
		
		return obj;
	}

}
