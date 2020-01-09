package com.mxc42.room_server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.mxc42.room_server.handler.AllOffHandler;
import com.mxc42.room_server.handler.DefaultHandler;
import com.mxc42.room_server.handler.HereHandler;
import com.mxc42.room_server.handler.LeavingHandler;
import com.mxc42.room_server.handler.OffHandler;
import com.mxc42.room_server.handler.OnHandler;
import com.mxc42.room_server.handler.ReadHandler;
import com.mxc42.room_server.handler.SpeakerHandler;
import com.mxc42.room_server.handler.ToggleHandler;
import com.mxc42.room_server.model.MainModel;
import com.mxc42.room_server.serial.SerialProxy;
import com.sun.net.httpserver.HttpServer;

public class App {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private static final int DEFAULT_PORT = 4224;
    
    /***
     * main method for the Family Map Server
     * @param args port the server will run on
     */
    public static void main(String args[]) {
        int port;

        if (args.length < 1) {
            port = DEFAULT_PORT;
            System.out.println("No port provided, running on default port: " + DEFAULT_PORT);
        } else {
            port = Integer.parseInt(args[0]);
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
            try {
            	MainModel.getSerialProxy1().closePort();
                System.out.println("CommPort 1 is closed");
            } catch (Exception e) { e.printStackTrace(); }
        }});

        new App().run(port);
    }

    /**
     * Starts running the HTTP server
     *
     * @param port port to run the HTTP server on
     */
    private void run(int port) {
        System.out.println("Initializing SerialProxy1 on /dev/ttyUSB0");

    	SerialProxy serialProxy1 = new SerialProxy("/dev/ttyUSB0");
    	MainModel.setSerialProxy1(serialProxy1);

        System.out.printf("Starting HTTP Server on port %d\n", port);

        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), MAX_WAITING_CONNECTIONS);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");

        //server.createContext("/user/register", new RegisterHandler());
        server.createContext("/", new DefaultHandler());
        server.createContext("/toggle", new ToggleHandler());
        server.createContext("/on", new OnHandler());
        server.createContext("/off", new OffHandler());
        server.createContext("/read", new ReadHandler());
        server.createContext("/speakers", new SpeakerHandler());
        server.createContext("/leaving", new LeavingHandler());
        server.createContext("/here", new HereHandler());
        server.createContext("/alloff", new AllOffHandler());

        System.out.println("Starting Server");
        server.start();
        System.out.println("Server started");
    }
}
