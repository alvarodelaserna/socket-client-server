package com.alvarodelaserna.socket.serverclient.main.server;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alvarodelaserna.socket.serverclient.R;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragmentView;
import com.alvarodelaserna.socket.serverclient.support.base.ViewNavigator;
import com.alvarodelaserna.socket.serverclient.support.ui.StringUtils;

public class ServerView extends BaseFragmentView {
	
	private TextView ipAddress, messageSent;
	private ViewListener viewListener;
	
	ServerView(ViewListener viewListener) {
		super(R.layout.server_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.server_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		ipAddress = (TextView) view.findViewById(R.id.server_ip_address);
		messageSent = (TextView) view.findViewById(R.id.server_message);
		Button getRadioManuallyButton = (Button) view.findViewById(R.id.server_get_radio_button);
		getRadioManuallyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onGetRadio();
			}
		});
	}
	
	void updateIpAddress(String ip) {
		ipAddress.setText(ip);
	}
	
	void updateMessage(String message) {
		messageSent.setText(message);
	}
	
	public void paintGetRadio(String radioData) {
		messageSent.setText(StringUtils.formatString(radioData));
	}
	
	public interface ViewListener {
		
		void onGetRadio();
	}
}
