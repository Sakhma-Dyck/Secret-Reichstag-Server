package me.mrletsplay.srweb.util;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;

public class SRWebConfig {
	
	private static FileCustomConfig config;
	
	static {
		config = ConfigLoader.loadFileConfig(new File("config.yml"));
		config.addDefault("ssl.enable", false);
		config.addDefault("ssl.certificate-path", "pem/cert.pem");
		config.addDefault("ssl.private-key-path", "pem/privkey.pem");
		config.addDefault("ssl.certificate-password", "");
		config.addDefault("server.insecure.host", "0.0.0.0");
		config.addDefault("server.insecure.port", 34642);
		config.addDefault("server.secure.host", "0.0.0.0");
		config.addDefault("server.secure.port", 34643);
		config.applyDefaults();
		config.saveToFile();
	}
	
	public static boolean isEnableSSL() {
		return config.getBoolean("ssl.enable");
	}
	
	public static String getCertificatePath() {
		return config.getString("ssl.certificate-path");
	}
	
	public static String getPrivateKeyPath() {
		return config.getString("ssl.private-key-path");
	}
	
	public static String getCertificatePassword() {
		return config.getString("ssl.certificate-password");
	}
	
	public static String getInsecureHost() {
		return config.getString("server.insecure.host");
	}
	
	public static int getInsecurePort() {
		return config.getInt("server.insecure.port");
	}
	
	public static String getSecureHost() {
		return config.getString("server.secure.host");
	}
	
	public static int getSecurePort() {
		return config.getInt("server.secure.port");
	}

}
