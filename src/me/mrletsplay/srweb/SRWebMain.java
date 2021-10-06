package me.mrletsplay.srweb;

import java.io.IOException;
import java.net.InetSocketAddress;

import me.mrletsplay.srweb.game.Player;
import me.mrletsplay.srweb.packet.Packet;
import me.mrletsplay.srweb.util.SRWebConfig;
import me.mrletsplay.srweb.util.SRWebSocketServer;

public class SRWebMain {
	
	private static SRWebSocketServer
		serverInsecure,
		serverSecure;
	
	public static void main(String[] args) throws IOException {
		serverInsecure = new SRWebSocketServer(new InetSocketAddress("0.0.0.0", 34642), false);
		serverSecure = SRWebConfig.isEnableSSL() ? new SRWebSocketServer(new InetSocketAddress("0.0.0.0", 34643), true) : null;
		
		SRWeb.PACKET_HANDLERS.forEach(p -> {
			serverInsecure.addHandler(p);
			if(serverSecure != null) serverSecure.addHandler(p);
		});
		
		serverInsecure.start();
		if(serverSecure != null) serverSecure.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				System.out.println("Stopping server...");
				serverInsecure.stop(1000);
				if(serverSecure != null) {
					System.out.println("Stopping secure server...");
					serverSecure.stop(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}));
		System.out.println("Server is online");
	}
	
	public static SRWebSocketServer getInsecureServer() {
		return serverInsecure;
	}
	
	public static SRWebSocketServer getSecureServer() {
		return serverSecure;
	}
	
	public static void handlePacket(Player player, Packet packet) {
		serverInsecure.handlePacket(player, packet);
		if(serverSecure != null) serverSecure.handlePacket(player, packet);
	}
	
}
