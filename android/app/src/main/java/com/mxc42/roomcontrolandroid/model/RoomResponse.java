package com.mxc42.roomcontrolandroid.model;

public class RoomResponse {
    private int port;
    private boolean state;

    public int getPort() {
        return port;
    }

    public boolean getState() {
        return state;
    }

    public String getValue() {
        if (state) {
            return "on";
        }
        return "off";
    }
}
