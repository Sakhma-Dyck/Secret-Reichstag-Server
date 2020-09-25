package me.mrletsplay.srweb;

import java.io.IOException;
import java.net.InetSocketAddress;

import me.mrletsplay.srweb.util.SRWebConfig;
import me.mrletsplay.srweb.util.SRWebSocketServer;

public class SRWebMain {
	
	public static void main(String[] args) throws IOException {
		SRWebSocketServer
			serverInsecure = new SRWebSocketServer(new InetSocketAddress("0.0.0.0", 34642), false),
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

}
