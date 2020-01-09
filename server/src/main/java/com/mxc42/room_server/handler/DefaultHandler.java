package com.mxc42.room_server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultHandler implements HttpHandler {
    private final String HTTP_ROOT = "/home/pi/room_control/web";
    private final String INDEX = "/index.html";
    private final String LOC_404 = "/HTML/404.html";

    /**
     * @param httpExchange the HttpExchange to handle
     * @throws IOException internal server error
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String pathString = uri.getPath();

        if (pathString.equals("/")) {
            pathString = INDEX;
        }

        int responseCode = 0;
        int bodyIsEmptyCode = 0;
        Path path = Paths.get(HTTP_ROOT + pathString);
        byte[] result = new byte[0];
        try {
            result = Files.readAllBytes(path);
            responseCode = HttpURLConnection.HTTP_OK;
            bodyIsEmptyCode = 0;
        } catch (IOException error404) {
            try {
                path = Paths.get(HTTP_ROOT + LOC_404);
                result = Files.readAllBytes(path);
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
            } catch (IOException cantRead404File) {
                bodyIsEmptyCode = -1;
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
            }
        }

        httpExchange.sendResponseHeaders(responseCode, bodyIsEmptyCode);
        OutputStream os = httpExchange.getResponseBody();
        os.write(result);
        os.close();

    }
}
