package com.shlomi.Net;

public interface DataTransferProtocolInterface {
	
	/**
	 * When client want to join, he asks for login. Header
	 */
	public final short HEADER_LOGIN_REQUEST = -10;
	
	/**
	 * Server can broadcast to all clients that 'Hey i need your position'. This is the header for this.
	 */
	public final short HEADER_REQUEST_POSITION = -11;
	
	/**
	 * This is called ONCE for every client. It only tells the server 'hey i need all players positions' for the first time.
	 */
	public final short HEADER_REQUEST_ALL_POSITIONS = -12;
}
