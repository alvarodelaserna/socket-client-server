package com.alvarodelaserna.socket.serverclient.main.client;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alvarodelaserna.socket.serverclient.R;
import com.alvarodelaserna.socket.serverclient.instruments.utils.StringUtils;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragmentView;
import com.alvarodelaserna.socket.serverclient.support.base.ViewNavigator;
import com.alvarodelaserna.socket.serverclient.support.ui.TextWatcher;
import com.alvarodelaserna.socket.serverclient.support.ui.ToastUtils;

public class ClientView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	private TextInputEditText ipAddressEditText, portEditText;
	private TextView receivedMessage;
	
	private String ipAddress, port;
	
	ClientView(ViewListener viewListener) {
		super(R.layout.client_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(final View view) {
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.client_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		Button connectButton = (Button) view.findViewById(R.id.client_connect_button);
		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					viewListener.onGetRadio(ipAddress, port);
				}
			}
		});
		Button clearButton = (Button) view.findViewById(R.id.client_clear_button);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onClearScreen();
			}
		});
		ipAddressEditText = (TextInputEditText) view.findViewById(R.id.client_address_edit_text);
		ipAddressEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				ipAddress = s.toString();
			}
		});
		portEditText = (TextInputEditText) view.findViewById(R.id.client_port_edit_text);
		portEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				port = s.toString();
			}
		});
		receivedMessage = (TextView) view.findViewById(R.id.client_response_text_view);
	}
	
	void updateMessageReceived(String response) {
		receivedMessage.setText(response);
	}
	
	void clearMessage() {
		receivedMessage.setText("");
	}
	
	interface ViewListener {
		
		void onGetRadio(String ipAddress, String port);
		
		void onClearScreen();
		
		void onResponseReceived(String response);
	}
	
}
