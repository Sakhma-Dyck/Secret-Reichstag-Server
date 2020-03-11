package me.mrletsplay.ssweb;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SSWebMain {
	
	public static void main(String[] args) throws IOException {
//		LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
//		PatternLayoutEncoder ple = new PatternLayoutEncoder();
//
//        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
//        ple.setContext(ctx);
//        ple.start();
//
//		
//		FileAppender<ILoggingEvent> appender = new FileAppender<>();
//		appender.setFile("log/debug.log");
//		appender.setImmediateFlush(true);
////		appender.setAppend(true);
//        appender.setEncoder(ple);
//		appender.setContext(ctx);
//		
//		Logger l = (Logger) LoggerFactory.getLogger(WebSocketImpl.class);
//		l.addAppender(appender);
//		System.out.println(l);
//		l.setLevel(Level.ALL);
//		l.info("TEST");
//		
//		Logger l2 = (Logger) LoggerFactory.getLogger(WebSocketServer.class);
//		l2.addAppender(appender);
//		System.out.println(l2);
//		l2.setLevel(Level.ALL);
//		l2.info("Test");
		
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
