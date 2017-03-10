package com.alvarodelaserna.socket.serverclient.support.ui;

import java.io.Serializable;

public class Request implements Serializable {
	
	public static final String GET_RADIO = "getRadio";
	public String value;
	
	public Request(String value) {
		this.value = value;
	}
}
