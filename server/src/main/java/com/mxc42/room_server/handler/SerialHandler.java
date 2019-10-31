package com.mxc42.room_server.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.mxc42.room_server.model.MainModel;
import com.mxc42.room_server.serial.SerialRequest;
import com.mxc42.room_server.serial.SerialResponse;
import com.sun.net.httpserver.HttpExchange;

public class SerialHandler {

	protected String[] parsePath(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSplit = path.split("/");

        return pathSplit;
    }
	
	protected void serialHandle(HttpExchange exchange, SerialRequest request) throws IOException {
		Gson gson = new Gson();
		
		String body = null;
        Integer code = null;
		
		String[] path = parsePath(exchange);
        int port;
        if (path.length > 2) {
        	port = Integer.parseInt(path[2]);
        	request.setPort(port);
        	
        	SerialResponse response = MainModel.getSerialProxy1().run(request);
        	body = gson.toJson(response, SerialResponse.class);
        	code = HttpURLConnection.HTTP_OK;
        }
        else {
        	throw new IOException("Invalid path");
        }
        
        exchange.sendResponseHeaders(code, body.getBytes(Charset.forName("UTF-8")).length);
        OutputStream outStream = exchange.getResponseBody();
        outStream.write(body.getBytes(Charset.forName("UTF-8")));
        outStream.close();
        System.out.println(body + " - " + code);
		
	}
}
