package com.shlomi;

import java.io.IOException;

import com.shlomi.Net.Client.ClientBroadcastReceiver;
import com.shlomi.Net.Client.ClientReceiver;
import com.shlomi.Net.Client.ClientSender;
import com.shlomi.pong.Initialization.StartGameWithID;

public final class StartNewClient {

	private static ClientReceiver clientReceiver;
	private static ClientSender clientSender;
	private static ClientBroadcastReceiver clientBroadcastReceiver;
	public static boolean IS_RUNNIG = false;
	
	public StartNewClient(String hostname) throws IOException {

		
		echo("Initializing client receiver...");
		clientReceiver = new ClientReceiver(hostname);
		echo("Client receiver initialized.");
		
		
		echo("Initializing client sender...");
		clientSender = new ClientSender(hostname);
		echo("Client sender initialized.");
		
		
		echo("Initializing client broadcast receiver...");
		clientBroadcastReceiver = new ClientBroadcastReceiver();
		echo("Client broadcast receiver initialized.");
		
		
		echo("Starting all client...");
		clientReceiver.start();
		clientBroadcastReceiver.start();
		clientSender.start();
		
		//Wait until all threads are safe
		while(true)
			if(clientReceiver.isRunning && clientBroadcastReceiver.isRunning && clientSender.isRunning)
				break;
		
		echo("Client is up and running.");
		
		//Send to that server ID request packet.
		short myID = clientSender.sendIDRequestAndReturnID();
		new StartGameWithID(myID).start();
		clientSender.sendMyPosition();
		
		clientSender.requestAllPositions();
		
		IS_RUNNIG = true;
	}
	
	public void echo(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Stops all threads.
	 */
	public static void stopThreads() {
		clientReceiver.stop();
		clientBroadcastReceiver.stop();
		clientSender.stop();
		System.exit(0);
	}
}
