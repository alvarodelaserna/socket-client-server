package com.alvarodelaserna.socket.serverclient.main.server;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.alvarodelaserna.socket.serverclient.R;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragmentView;
import com.alvarodelaserna.socket.serverclient.support.base.ViewNavigator;

public class ServerView extends BaseFragmentView {
	
	private TextView ipAddress, messageSent;
	
	ServerView() {
		super(R.layout.server_layout);
	}
	
	@Override
	protected void setUpView(View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.server_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		ipAddress = (TextView) view.findViewById(R.id.server_ip_address);
		messageSent = (TextView) view.findViewById(R.id.server_message);
	}
	
	void updateIpAddress(String ip) {
		ipAddress.setText(ip);
	}
	
	void updateMessage(String message) {
		messageSent.setText(message);
	}
	
}
