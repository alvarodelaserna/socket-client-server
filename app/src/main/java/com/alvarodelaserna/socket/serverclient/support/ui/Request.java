package com.alvarodelaserna.socket.serverclient.support.ui;

import java.io.Serializable;

public class Request implements Serializable {
	
	public static final String GET_RADIO = "getRadio";
	public static final String SET_REGISTER_OFF = "setRegisterOff";
	public static final String SET_REGISTER_ON = "setRegisterOn";
	public static final String CONNECT = "connect";
	public String value;
	
	public Request(String value) {
		this.value = value;
	}
}
