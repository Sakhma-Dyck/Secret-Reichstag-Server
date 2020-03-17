package me.mrletsplay.srweb;

import java.io.IOException;
import java.net.InetSocketAddress;

import me.mrletsplay.srweb.util.SRWebSocketServer;

public class SRWebMain {
	
	public static void main(String[] args) throws IOException {
		SRWebSocketServer websocketServer = new SRWebSocketServer(new InetSocketAddress("0.0.0.0", 34643));
		SRWeb.PACKET_HANDLERS.forEach(websocketServer::addHandler);
		websocketServer.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				System.out.println("Stopping server...");
				websocketServer.stop(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}));
		System.out.println("Server is online");
	}

}
