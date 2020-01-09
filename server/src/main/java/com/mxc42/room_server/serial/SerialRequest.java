package com.mxc42.room_server.serial;

public class SerialRequest {
	private int port;
	private char mode;
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the mode
	 */
	public char getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(char mode) {
		this.mode = mode;
	}

	public String toSerialStr() {
		return new String("<" + mode + ',' + Integer.toString(port) + '>');
	}
}
