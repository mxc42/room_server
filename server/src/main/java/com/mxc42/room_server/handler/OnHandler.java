package com.mxc42.room_server.handler;

import java.io.IOException;

import com.mxc42.room_server.serial.SerialProxy;
import com.mxc42.room_server.serial.SerialRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class OnHandler extends SerialHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		SerialRequest request = new SerialRequest();

		request.setMode(SerialProxy.MODE_ON);
		
		serialHandle(exchange, request);
	}
}
