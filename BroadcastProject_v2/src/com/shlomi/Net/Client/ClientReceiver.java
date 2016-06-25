package com.shlomi.Net.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.shlomi.Net.SocketHelperSystem;

/**
 * Client will receive ONLY DatagramPackets from DatagramSockets.
 * @author Shlomi
 *
 */

public class ClientReceiver extends SocketHelperSystem{

	private DatagramSocket socket;
	private InetSocketAddress serverReceiverAddress;
	private final String TITLE = ClientReceiver.class.getSimpleName();
	public boolean isRunning;
	
	public ClientReceiver(String hostname) throws UnknownHostException, SocketException {
		super();
		
		Thread.currentThread().setName(TITLE);
		
		//We don't care about whats OUR ip / port..
		socket = new DatagramSocket();
		//Try to connect to server receiver.
		serverReceiverAddress = new InetSocketAddress(hostname, SERVER_RECEIVER_PORT);
		socket.connect(serverReceiverAddress);
		echo(TITLE,"Connected to server receiver: " + serverReceiverAddress.getAddress() + ":" + serverReceiverAddress.getPort());
	}
	
	public void run() {
		
		echo(TITLE,"Running...");
		isRunning = true;
		while(true) {

			
			try {
				byte[] dataToReceive = new byte[BYTE_ARRAY_ALLOWED];
				DatagramPacket packet = new DatagramPacket(dataToReceive, dataToReceive.length);
				socket.receive(packet);
				byte[] dataReceived = packet.getData();
				short HEADER = getShortByBytesArray(dataReceived);
				echo(TITLE,"Received packet header: " + HEADER);
			} catch (IOException e) {
				e.printStackTrace();
			}
			

		}
	}
}
