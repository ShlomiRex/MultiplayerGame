package com.shlomi.Net.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.shlomi.Net.SocketHelperSystem;
import com.shlomi.Net.Server.ServerReceiver;
import com.shlomi.pong.Initialization.Game;

/**
 * Client sender class will send DatagramPackets to DatagramSocket. 
 * @see ServerReceiver
 * @author Shlomi
 *
 */

public class ClientSender extends SocketHelperSystem {

	private static DatagramSocket socket;
	public static InetSocketAddress receiverServerSocketAddress;
	private final String TITLE = ClientSender.class.getSimpleName();
	public boolean isRunning;
	
	private static short ID = -1;
	
	public ClientSender(String hostName) throws IOException {
		super();
		
		Thread.currentThread().setName(TITLE);
		
		//We do not care which port / ip we use.
		socket = new DatagramSocket();
		
		//Get the server's address for receiving packets.
		receiverServerSocketAddress = new InetSocketAddress(hostName, SERVER_RECEIVER_PORT);
		echo(TITLE,"Receiver server socket address: " + receiverServerSocketAddress.getAddress() + ":" +receiverServerSocketAddress.getPort());
		
		echo(TITLE,"Trying to connect to receiver server...");
		socket.connect(receiverServerSocketAddress);
		echo(TITLE,"Connected!");
	}
	
	public void run() {
		
		echo(TITLE,"Running...");
		isRunning = true;
		while(true) {
			//Wait
		}
	}
	
	/**
	 * Send to server receiver an 'ID request packet' and then receive from it ID.<br>
	 * After than send the first time position to server.
	 * @return The ID that got from server.
	 * @throws IOException
	 */
	public short sendIDRequestAndReturnID() throws IOException {
		echo(TITLE,"Sending ID request packet.");
		DatagramPacket packet = getIDRequestPacket();
		packet.setAddress(receiverServerSocketAddress.getAddress());
		packet.setPort(SERVER_RECEIVER_PORT);
		socket.send(packet);
		
		byte[] dataToReceive = new byte[BYTE_ARRAY_ALLOWED];
		packet = new DatagramPacket(dataToReceive, dataToReceive.length);
		socket.setSoTimeout(ID_RECEIVE_TIMEOUT);
		socket.receive(packet);
		short myID = getShortByBytesArray(packet.getData());
		echo(TITLE,"Client received from server ID of : " + myID);
		
		ID = myID;
		return myID;
	}
	
	/**
	 * Send to server the player's position.
	 * @throws IOException 
	 */
	public void sendMyPosition() throws IOException {
		short myX = Game.getX();
		short myY = Game.getY();
		send(ID, myX, myY);
	}
	
	/**
	 * Send packet to receiver server.
	 * @param header
	 * @param X
	 * @param Y
	 */
	public static void send(short header, short X, short Y) {
		byte[] data = assembleData(header, X, Y);
		DatagramPacket packet = new DatagramPacket(data, data.length, receiverServerSocketAddress);
		
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Request from server all player's positions.<br>
	 * This is called only ONCE !
	 */
	public void requestAllPositions() {
		send(HEADER_REQUEST_ALL_POSITIONS,(short)0,(short)0);
	}

	
	
	
	
	
	
	
	
	
	
}

