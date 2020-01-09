package com.mxc42.room_server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.mxc42.room_server.model.MainModel;
import com.mxc42.room_server.serial.SerialProxy;
import com.mxc42.room_server.serial.SerialRequest;
import com.mxc42.room_server.serial.SerialResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AllOffHandler implements HttpHandler {
	SerialProxy serialProxy;

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Integer code = null;
		String body = null;
		serialProxy = MainModel.getSerialProxy1();

		body = "{starting all off}";
		new Thread(new AllOffThread()).start();

		code = HttpURLConnection.HTTP_OK;
		exchange.sendResponseHeaders(code, body.getBytes(Charset.forName("UTF-8")).length);
		OutputStream outStream = exchange.getResponseBody();
		outStream.write(body.getBytes(Charset.forName("UTF-8")));
		outStream.close();
	}

	class AllOffThread implements Runnable {

		@Override
		public void run() {
			SerialRequest request = new SerialRequest();

			try {
				for (int i = 0; i <= SerialProxy.PORT_MAIN_LIGHT; i++) {
					request.setPort(i);
					request.setMode(SerialProxy.MODE_READ);
					SerialResponse readResponse = serialProxy.run(request);
					if (readResponse.isState()) {
						Thread.sleep(500);
						request.setMode(SerialProxy.MODE_OFF);
						serialProxy.run(request);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
