package me.mrletsplay.ssweb;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SSWebMain {
	
	public static void main(String[] args) throws IOException {
		SSWebSocketServer ssS = new SSWebSocketServer(new InetSocketAddress("0.0.0.0", 34643));
		SSWeb.PACKET_HANDLERS.forEach(ssS::addHandler);
		ssS.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				System.out.println("STOP");
				ssS.stop(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}));
		System.out.println("started");
	}

}
