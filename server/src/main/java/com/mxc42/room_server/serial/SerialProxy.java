package com.mxc42.room_server.serial;

import com.fazecast.jSerialComm.SerialPort;

public class SerialProxy {
	public static final char MODE_TOGGLE = 't';
	public static final char MODE_ON = 'i';
	public static final char MODE_OFF = 'o';
	public static final char MODE_READ = 'r';
	
	public static final int PORT_HALL_LIGHT = 0;
	public static final int PORT_1 = 1;
	public static final int PORT_2 = 2;
	public static final int PORT_3 = 3;
	public static final int PORT_MODE_MIXER = 4;
	public static final int PORT_5 = 5;
	public static final int PORT_SUBWOOFER = 6;
	public static final int PORT_AMP = 7;
	public static final int PORT_MAIN_LIGHT = 8;

	private SerialPort comPort;
	
	public SerialProxy(String port) {
		comPort = SerialPort.getCommPort(port);
		comPort.setBaudRate(9600);
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 200, 0);
		comPort.openPort();
	}
	
	public SerialResponse run(SerialRequest request) {

		String sendString = request.toSerialStr();

		comPort.writeBytes(sendString.getBytes(), sendString.length());

		byte[] readBuffer = new byte[100];
		comPort.readBytes(readBuffer, readBuffer.length);

		SerialResponse response = new SerialResponse();

		if (readBuffer[0] == (byte)request.getMode()) {
			response.setPort((int)readBuffer[1] - 48);
			response.setState(readBuffer[2] != 48);
		}

		return response;
	}

	public void closePort() {
		comPort.closePort();
	}
}
