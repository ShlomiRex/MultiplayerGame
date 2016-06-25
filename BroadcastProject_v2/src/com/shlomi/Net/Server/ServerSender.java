package com.shlomi.Net.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.shlomi.Net.SocketHelperSystem;

/**
 * Server sends whatever need to be sent. It's new thread so receive & sending will be faster without interrupt.
 * @author Shlomi
 *
 */
public final class ServerSender extends SocketHelperSystem{

	private final String TITLE = ServerSender.class.getSimpleName();
	private DatagramSocket socket;
	public short CURRENT_ID = 0;
	public boolean isRunning;
	
	public ServerSender() throws IOException {
		super();
		
		Thread.currentThread().setName(TITLE);
		
		socket = new DatagramSocket();
		echo(TITLE,"Server receiver is : " + socket.getLocalAddress() + ":" + socket.getLocalPort());
	}
	
	public void run() {
		echo(TITLE,"..........Server can now send packets.");
		
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
		echo(TITLE,"Header = " + packetHeader);
		switch(packetHeader) {
			case HEADER_LOGIN_REQUEST:
				sendIDPacket(packet);
				break;
		}
	}
	
	private void sendIDPacket(DatagramPacket packetWithIDRequest) {
		try {
			echo(TITLE,"Sending ID packet to client...");
			//Get client's address and port and send packet to him.
			InetAddress address = packetWithIDRequest.getAddress();
			int port = packetWithIDRequest.getPort();
			
			//Create datagram packet to send to client with ID he requested.
			//This is working good.
			byte[] dataToSend = assembleData(CURRENT_ID, (short)0, (short)0);
			DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, address, port);
			
			//Finnaly, send
			socket.send(packetToSend);
			
			//Increase current id count.
			CURRENT_ID++;
			echo(TITLE,"Done!");
		} catch (IOException e) {
		}
		
		
	}
	
	
	public String getInfo() {
		return socket.getLocalAddress() + ":" + socket.getLocalPort();
	}
	
	
}
