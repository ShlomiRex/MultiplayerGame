package com.shlomi.Net.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import com.shlomi.Net.IPsAndPorts;
import com.shlomi.Net.SocketHelperSystem;

/**
 * Mission: Send again and again ALL client's positions.
 * @author Shlomi
 *
 */
public final class ServerBroadcast extends SocketHelperSystem implements IPsAndPorts{
	
	private static MulticastSocket socket;
	private static final String TITLE = SocketHelperSystem.class.getSimpleName();
	public boolean isRunning = false;
	
	public ServerBroadcast() throws IOException {
		
		Thread.currentThread().setName(TITLE);
		
		socket = new MulticastSocket(BROADCAST_GROUP_PORT);
		joinBroadcastGroup(socket);
		
	}
	
	public void run() {
		echo(TITLE,"Running...");
		isRunning = true;
		while(true) {
			//Wait until broadcast method is called...
		}
	}//run
	
	/**
	 * Broadcast.
	 * @param packet What to broadcast.
 	 */
	public static void broadcast(DatagramPacket packet) {
		byte[] data = packet.getData();
		echo(TITLE,"Broadcasting: " + getStringByByteArray(data));
		DatagramPacket packetToSend = new DatagramPacket(data, data.length, groupSocketAddress);
		try {
			socket.send(packetToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getInfo() {
		return socket.getLocalAddress() + ":" + socket.getLocalPort();
	}

}
