package com.shlomi.Net.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import com.shlomi.Net.IPsAndPorts;
import com.shlomi.Net.SocketHelperSystem;
import com.shlomi.pong.Initialization.Game;

/**
 * Client will receive ONLY DatagramPackets from the Broadcast.
 * @author Shlomi
 *
 */

public final class ClientBroadcastReceiver extends SocketHelperSystem implements IPsAndPorts{
	
	private MulticastSocket socket;
	private final String TITLE = ClientBroadcastReceiver.class.getSimpleName();
	public boolean isRunning;
	
	public ClientBroadcastReceiver() throws IOException {
		
		Thread.currentThread().setName(TITLE);
		
		//Bind socket's port to broad cast group, that, when we join the group, we listen ONLY to that port!
		socket = new MulticastSocket(BROADCAST_GROUP_PORT);
		
		joinBroadcastGroup(socket);
	}
	
	public void run() {
		
		echo(TITLE,"Running...");
		isRunning = true;
		while(true) {
			try {
				byte[] data = new byte[BYTE_ARRAY_ALLOWED];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				socket.receive(packet);
				echo(TITLE,"Received a packet: " + getStringByByteArray(packet.getData()));
				short[] values = disassembleData(packet.getData());
				
				switch(values[0]) {
				case HEADER_REQUEST_POSITION:
					Game.sendToServerMyPosition();
					break;
					
					default:
						Game.updateFromBroadcast(disassembleData(packet.getData()));
						break;
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
