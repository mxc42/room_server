package com.mxc42.roomcontrolandroid.model;

public class RoomRequest {
    private String path;

    public RoomRequest() {
    }

    public RoomRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
