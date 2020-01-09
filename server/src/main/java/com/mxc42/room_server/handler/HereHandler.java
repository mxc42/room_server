package com.mxc42.room_server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.mxc42.room_server.model.MainModel;
import com.mxc42.room_server.serial.SerialProxy;
import com.mxc42.room_server.serial.SerialRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HereHandler implements HttpHandler {
	SerialProxy serialProxy;

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Integer code = null;
		String body = null;
		serialProxy = MainModel.getSerialProxy1();

		body = "{starting here}";
		new Thread(new HereThread()).start();

		code = HttpURLConnection.HTTP_OK;
		exchange.sendResponseHeaders(code, body.getBytes(Charset.forName("UTF-8")).length);
		OutputStream outStream = exchange.getResponseBody();
		outStream.write(body.getBytes(Charset.forName("UTF-8")));
		outStream.close();
	}

	class HereThread implements Runnable {

		@Override
		public void run() {
			SerialRequest request = new SerialRequest();
			request.setMode(SerialProxy.MODE_OFF);
			request.setPort(SerialProxy.PORT_HALL_LIGHT);
			serialProxy.run(request);
			request.setMode(SerialProxy.MODE_ON);
			request.setPort(SerialProxy.PORT_MAIN_LIGHT);
			serialProxy.run(request);
			try {
				Thread.sleep(400);
				request.setMode(SerialProxy.MODE_ON);
				request.setPort(SerialProxy.PORT_AMP);
				serialProxy.run(request);
				Thread.sleep(400);
				request.setPort(SerialProxy.PORT_SUBWOOFER);
				serialProxy.run(request);
				Thread.sleep(400);
				request.setPort(SerialProxy.PORT_MODE_MIXER);
				serialProxy.run(request);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
