package me.mrletsplay.srweb.util;

import java.io.File;

import me.mrletsplay.mrcore.config.ConfigLoader;
import me.mrletsplay.mrcore.config.FileCustomConfig;

public class SRWebConfig {
	
	private static FileCustomConfig config;
	
	static {
		config = ConfigLoader.loadFileConfig(new File("config.yml"));
		config.addDefault("ssl.enable", true);
		config.addDefault("ssl.certificate-path", "pem/cert.pem");
		config.addDefault("ssl.private-key-path", "pem/privkey.pem");
		config.addDefault("ssl.certificate-password", "");
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

}
