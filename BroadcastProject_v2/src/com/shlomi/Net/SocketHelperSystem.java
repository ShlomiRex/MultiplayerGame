package com.shlomi.Net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class SocketHelperSystem extends Thread implements IPsAndPorts, DataTransferProtocolInterface, Settings{ 

	public static InetSocketAddress groupSocketAddress;
	public final static int BYTE_ARRAY_ALLOWED = 6;
	
	public SocketHelperSystem() throws UnknownHostException {
		groupSocketAddress = new InetSocketAddress(InetAddress.getByName(BROADCAST_GROUP_IP), BROADCAST_GROUP_PORT);
	}
	
	public static void echo(String title, String msg) {
		System.out.println("[" + title + "]..." + msg);
	}
	
	public void joinBroadcastGroup(MulticastSocket socket) throws IOException {
		echo("","Joining broadcast group...");
		socket.joinGroup(groupSocketAddress.getAddress());
		echo("","Joined broadcast group: " + groupSocketAddress.getAddress());
	}
	
	public void leaveBroadcastGroup(MulticastSocket socket) throws IOException {
		echo("","Leaving group...");
		socket.leaveGroup(groupSocketAddress.getAddress());
		echo("","Left the group.");
	}
	
	/**
	 * 
	 * @return Packet with ID request header. <br>
	 * <b>NOTE: This packet is not bounded to anything. To send it you first need to give it IP and PORT.</b>
	 */
	public DatagramPacket getIDRequestPacket() {
		byte[] data = assembleData(HEADER_LOGIN_REQUEST, (short)0, (short)0);
		return new DatagramPacket(data, data.length);
	}
	
	/**
	 * Return the bytes that will go into packet and server / client will receive it.
	 * After that we need to disassemble into 2 short values.
	 * @param x For this app, its X position
	 * @param y For this app, its Y position
	 * @param ID For this app, its ID
	 * @return byte[] array which socket will receive, then disassemble.
	 */
	public static byte[] assembleData(short HEADER, short VALUE1, short VALUE2) {
		byte[] result = getStandardDataByteArray();
		
		//Get bytes array from x,y short pritimitive values
		byte[] arr1 = getBytesArrayByShort(HEADER);
		byte[] arr2 = getBytesArrayByShort(VALUE1);
		byte[] arr3 = getBytesArrayByShort(VALUE2);
		
		//Combine arrays together into result array
		result[0] = arr1[0];
		result[1] = arr1[1];
		result[2] = arr2[0];
		result[3] = arr2[1];
		result[4] = arr3[0];
		result[5] = arr3[1];
		
		//Send the final result
		return result;
	}

	/**
	 * Convert short to bytes array.
	 * @param bytes
	 * @return byte array of size 2.
	 */
	public static byte[] getBytesArrayByShort(short value) {
	    return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
	}

	private static byte[] getStandardDataByteArray() {
		return new byte[BYTE_ARRAY_ALLOWED];
	}
	
	/**
	 * Convert bytes array to short primitive type.
	 * @param bytes <b>Byte array of 2.</b>
	 * @return short primitive data.
	 */
	public static short getShortByBytesArray(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	/**
	 * Disassemble packet bytes array to 3 short primitive data types.
	 * <br><b>NOTE: Data array size must be 4.<b>
	 * @param data Data to disassemble.
	 * @return Short array with length of 2. In position 0 of the array, a short value is stored for X.
	 * In the position 1 of the array, a short value is stored for y.<br><br>
	 * Can return 'null' if data array size is not 4.
	 */
	public static short[] disassembleData(byte[] data) {
		if(data.length != BYTE_ARRAY_ALLOWED)
			return null;
		
		short[] result = new short[3];
		
		byte[] bytes1 = new byte[2];
		byte[] bytes2 = new byte[2];
		byte[] bytes3 = new byte[2];
		
		//Split data into 2 arrays. Each array length is 2. 
		//Output the copyied array into bytes1 and bytes2 arrays.
		
		bytes1[0] = data[0];
		bytes1[1] = data[1];
		bytes2[0] = data[2];
		bytes2[1] = data[3];
		bytes3[0] = data[4];
		bytes3[1] = data[5];
		
		//Transform byte array into short primitive type.
		result[0] = getShortByBytesArray(bytes1);
		result[1] = getShortByBytesArray(bytes2);
		result[2] = getShortByBytesArray(bytes3);
		
		return result;
	}
	
	public static String getStringByByteArray(byte[] data) {
		short[] values = disassembleData(data);
		return "["+values[0] + "] [" + values[1] + "," + values[2] + "]";
	}
	
}
