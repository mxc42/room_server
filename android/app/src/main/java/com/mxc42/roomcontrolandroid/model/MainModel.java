package com.mxc42.roomcontrolandroid.model;

public class MainModel {
    private static MainModel instance = null;

    private boolean[] ports;

    private MainModel() {
        ports = new boolean[9];
    }

    private static MainModel getInstance() {
        if (instance == null) {
            instance = new MainModel();
        }
        return instance;
    }

    public static void setPorts(boolean[] ports) {
        getInstance().ports = ports;
    }

    public static void setPort(int port, boolean state) {
        getInstance().ports[port] = state;
    }

    public static boolean getPort(int port) {
        return getInstance().ports[port];
    }
}
