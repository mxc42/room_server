package com.mxc42.room_server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.mxc42.room_server.model.MainModel;
import com.mxc42.room_server.serial.SerialProxy;
import com.mxc42.room_server.serial.SerialRequest;
import com.mxc42.room_server.serial.SerialResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SpeakerHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
        Integer code = null;
        String body = null;
        Gson gson = new Gson();
        SerialProxy serialProxy = MainModel.getSerialProxy1();

        SerialRequest request = new SerialRequest();
        request.setMode(SerialProxy.MODE_TOGGLE);
        request.setPort(SerialProxy.PORT_MODE_MIXER);
        SerialResponse firstResponse = serialProxy.run(request);
        if (firstResponse.isState()) {
        	request.setMode(SerialProxy.MODE_ON);
        }
        else {
        	request.setMode(SerialProxy.MODE_OFF);
        }
        body = gson.toJson(firstResponse, SerialResponse.class);
        request.setPort(SerialProxy.PORT_AMP);
        body += gson.toJson(serialProxy.run(request), SerialResponse.class);
        request.setPort(SerialProxy.PORT_SUBWOOFER);
        body += gson.toJson(serialProxy.run(request), SerialResponse.class);

        code = HttpURLConnection.HTTP_OK;
        exchange.sendResponseHeaders(code, body.getBytes(Charset.forName("UTF-8")).length);
        OutputStream outStream = exchange.getResponseBody();
        outStream.write(body.getBytes(Charset.forName("UTF-8")));
        outStream.close();
	}
}
