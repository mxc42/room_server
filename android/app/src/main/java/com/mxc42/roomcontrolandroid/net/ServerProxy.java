package com.mxc42.roomcontrolandroid.net;

import com.google.gson.Gson;
import com.mxc42.roomcontrolandroid.model.RoomRequest;
import com.mxc42.roomcontrolandroid.model.RoomResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * server proxy handles all communication with the server. (registering, logging in, getting data).
 */
public class ServerProxy {
    private static final String IP_ADDRESS = "http://136.36.111.27:4224";
    private static final String TAG = "ServerProxy";
    private static final String ENCODING = "UTF-8";
    private static final int TIME_OUT = 3000;
    //private static final String AUTHTOKEN_HEADER = "Authorization";

    /**
     * Sends an run request to the server and returns the response
     * @param request the eventRequest to form a request from
     * @return null if failed, otherwise the response
     */
    public RoomResponse run(RoomRequest request) {
        Gson gson = new Gson();

        try {
            URL url = new URL(IP_ADDRESS + request.getPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setRequestMethod("POST");
            //connection.setRequestProperty(AUTHTOKEN_HEADER, request.getAuthToken());
            connection.connect();

            String responseBody = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                responseBody = getResponseBody(connection.getInputStream());
                RoomResponse response = gson.fromJson(responseBody, RoomResponse.class);

                return response;
            }
            else if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getResponseBody(InputStream responseBody) throws IOException {

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = responseBody.read(buffer)) != -1) {
            byteArrayOS.write(buffer, 0, length);
        }

        return byteArrayOS.toString();
    }

    private void setBody(HttpURLConnection connection, String body) throws IOException {
        byte[] byteArray = body.getBytes(ENCODING);
        OutputStream os = connection.getOutputStream();
        os.write(byteArray);
        os.close();
    }
}
