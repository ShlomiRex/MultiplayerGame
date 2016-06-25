package com.shlomi.Net.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.shlomi.Net.SocketHelperSystem;

/**
 * Server receives DatagramPackets from DatagramSockets ONLY.
 * @author Shlomi
 *
 */
public final class ServerReceiver extends SocketHelperSystem{

	private final String TITLE = ServerReceiver.class.getSimpleName();
	private DatagramSocket socket;
	public short CURRENT_ID = 0;
	public boolean isRunning;
	
	public ServerReceiver(String bindToThisAddress) throws IOException {
		super();
		
		Thread.currentThread().setName(TITLE);
		
		socket = new DatagramSocket(new InetSocketAddress(bindToThisAddress, SERVER_RECEIVER_PORT));
		echo(TITLE,"Server receiver is : " + getInfo());
	}
	
	public void run() {
		echo(TITLE,"..........Server will now receive packets.");
		byte[] data = new byte[BYTE_ARRAY_ALLOWED];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		isRunning = true;
		while(true) {
			try {
				
				//Receive packet
				socket.receive(packet);
				//Proccess the packet
				processPacket(packet);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//while true
	}

	private void processPacket(DatagramPacket packet) {
		echo(TITLE,"Proccessing received packet...");
		
		short packetHeader = getShortByBytesArray(packet.getData());
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		
		switch(packetHeader) {
			case HEADER_LOGIN_REQUEST:
				
				echo(TITLE,"Sending ID packet to client...");
				sendIDPacket(address, port);
				echo(TITLE,"ID sent.");
				
				break;
				
			case HEADER_REQUEST_ALL_POSITIONS:
				
				//Now ask ALL clients to send their position in order to show them for first time.
				echo(TITLE,"Requesting from all clients for position...");
				byte[] data = assembleData(HEADER_REQUEST_POSITION, (short)0, (short)0);
				DatagramPacket positionRequestPacket = new DatagramPacket(data, data.length);
				ServerBroadcast.broadcast(positionRequestPacket);
				echo(TITLE,"Asked for position.");
				
				break;
				
			default:
				//Position packet header MUST be positive. (Or 0)
				if(packetHeader < 0)
					break;
				
				//Broadcast packet's position.
				ServerBroadcast.broadcast(packet);
				break;
		}
		

	}
	
	/**
	 * Send packet to client with ID.
	 * @param packetWithIDRequest
	 */
	private void sendIDPacket(InetAddress address , int port) {
		try {
			//Create datagram packet to send to client with ID he requested.
			//This is working good.
			byte[] dataToSend = assembleData(CURRENT_ID, (short)0, (short)0);
			DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, address, port);
			
			//Finnaly, send
			socket.send(packetToSend);
			
			//Increase current id count.
			CURRENT_ID++;
		} catch (IOException e) {
		}
	}
	
	public String getInfo() {
		return socket.getLocalAddress() + ":" + socket.getLocalPort();
	}
}
