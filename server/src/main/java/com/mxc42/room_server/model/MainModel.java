package com.mxc42.room_server.model;

import com.mxc42.room_server.serial.SerialProxy;

public class MainModel {
	private static MainModel instance;

	private SerialProxy serialProxy1;

	private MainModel() {
	}
	
	public static synchronized MainModel getInstance() {
		if (instance == null) {
			instance = new MainModel();
		}
		return instance;
	}

	/**
	 * @return the serialProxy1
	 */
	public static SerialProxy getSerialProxy1() {
		return getInstance().serialProxy1;
	}

	/**
	 * @param serialProxy1 the serialProxy1 to set
	 */
	public static void setSerialProxy1(SerialProxy serialProxy1) {
		getInstance().serialProxy1 = serialProxy1;
	}
}
