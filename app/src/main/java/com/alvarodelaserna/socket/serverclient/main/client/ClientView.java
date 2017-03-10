package com.alvarodelaserna.socket.serverclient.main.client;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alvarodelaserna.socket.serverclient.R;
import com.alvarodelaserna.socket.serverclient.support.ui.KeyBoardHelper;
import com.alvarodelaserna.socket.serverclient.support.ui.StringUtils;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragmentView;
import com.alvarodelaserna.socket.serverclient.support.base.ViewNavigator;
import com.alvarodelaserna.socket.serverclient.support.ui.TextWatcher;
import com.alvarodelaserna.socket.serverclient.support.ui.ToastUtils;

public class ClientView extends BaseFragmentView {
	
	private final ViewListener viewListener;
	private TextInputEditText ipAddressEditText, portEditText;
	private TextView receivedMessage;
	
	private String ipAddress, port;
	private View mainView;
	
	ClientView(ViewListener viewListener) {
		super(R.layout.client_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(final View view) {
		mainView = view;
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.client_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		Button getRadioButton = (Button) view.findViewById(R.id.client_get_radio_button);
		getRadioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					hideKeyboard();
					viewListener.getRadio(ipAddress, port);
				}
			}
		});
		Button turnOffNetworkButton = (Button) view.findViewById(
			R.id.client_turn_off_network_button);
		turnOffNetworkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					hideKeyboard();
					viewListener.turnOffNetwork(ipAddress, port);
				}
			}
		});
		Button turnOnNetworkButton = (Button) view.findViewById(R.id.client_turn_on_network_button);
		turnOnNetworkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					hideKeyboard();
					viewListener.turnOnNetwork(ipAddress, port);
				}
			}
		});
		Button clearButton = (Button) view.findViewById(R.id.client_clear_button);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard();
				viewListener.clearScreen();
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
	
	private void hideKeyboard() {
		KeyBoardHelper.hideKeyBoard(viewContextInject(Context.class), mainView);
	}
	
	void updateMessageReceived(String response) {
		receivedMessage.setText(response);
	}
	
	void clearMessage() {
		receivedMessage.setText("");
	}
	
	interface ViewListener {
		
		void clearScreen();
		
		void onResponseReceived(String response);
		
		void getRadio(String ipAddress, String port);
		
		void turnOffNetwork(String ipAddress, String port);
		
		void turnOnNetwork(String ipAddress, String port);
	}
	
}
