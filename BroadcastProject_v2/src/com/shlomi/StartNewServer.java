package com.shlomi;

import java.io.IOException;

import com.shlomi.Initialization.OtherWindows.ServerShutdownWindow;
import com.shlomi.Net.Server.ServerBroadcast;
import com.shlomi.Net.Server.ServerReceiver;
import com.shlomi.Net.Server.ServerSender;

public class StartNewServer {
	
	
	private static ServerBroadcast broadCastServer;
	private static ServerReceiver receiverServer;
	private static ServerSender senderServer;

	public StartNewServer(String bindToThisAddress) throws IOException {
		
		
		echo("\n\n\nStarting broadcast server...");
		broadCastServer = new ServerBroadcast();
		echo("Broadcast server started.");
		
		
		echo("\n\n\nStarting receiver server...");
		receiverServer = new ServerReceiver(bindToThisAddress);
		echo("Receiver server started.");
		
		
		echo("\nStarting sender server...\n");
		senderServer = new ServerSender();
		echo("Sender server started.");
		
		echo("Starting all server...");
		broadCastServer.start();
		receiverServer.start();
		senderServer.start();
		
		while(true) {
			if(broadCastServer.isRunning && receiverServer.isRunning && senderServer.isRunning)
				break;
		}
		
		echo("Server is up and running.");
		ServerShutdownWindow window = new ServerShutdownWindow(broadCastServer.getInfo(),receiverServer.getInfo(), senderServer.getInfo());
		
		//initializeServerShutdownWindow();
	}
	
	public static void echo(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Stops all threads.
	 */
	public static void shutdownServer() {
		broadCastServer.stop();
		receiverServer.stop();
		senderServer.stop();
		echo("All server threads stopped. Good byte!");
		System.exit(0);
	}
}
