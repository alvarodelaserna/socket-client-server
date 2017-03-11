package com.alvarodelaserna.socket.serverclient.main.client;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
	private LinearLayout requestButtonsContainer;
	private Button connectButton;
	
	ClientView(ViewListener viewListener) {
		super(R.layout.client_layout);
		this.viewListener = viewListener;
	}
	
	@Override
	protected void setUpView(final View view) {
		mainView = view;
		Toolbar toolbar = (Toolbar) view.findViewById(R.id.client_toolbar);
		viewContextInject(ViewNavigator.class).setUpNavigation(toolbar);
		connectButton = (Button) view.findViewById(R.id.client_connect_button);
		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					hideKeyboard();
					viewListener.connect(ipAddress, port);
				}
			}
		});
		requestButtonsContainer = (LinearLayout) view.findViewById(
			R.id.client_request_buttons_container);
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
		Button setRegisterOffButton = (Button) view.findViewById(
			R.id.client_set_register_off_button);
		setRegisterOffButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					hideKeyboard();
					viewListener.setRegisterOff(ipAddress, port);
				}
			}
		});
		Button setRegisterOnButton = (Button) view.findViewById(R.id.client_set_register_on_button);
		setRegisterOnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNullOrEmpty(ipAddress) || StringUtils.isNullOrEmpty(port)) {
					ToastUtils.showShort(viewContextInject(Context.class),
										 viewContextInject(Context.class).getString(
											 R.string.empty_fields));
				} else {
					hideKeyboard();
					viewListener.setRegisterOn(ipAddress, port);
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
	
	void enableRequestButtons() {
		connectButton.setVisibility(View.GONE);
		requestButtonsContainer.setVisibility(View.VISIBLE);
	}
	
	public void disableInputs() {
		ipAddressEditText.setEnabled(false);
		portEditText.setEnabled(false);
	}
	
	public interface ViewListener {
		
		void clearScreen();
		
		void onResponseReceived(String response);
		
		void getRadio(String ipAddress, String port);
		
		void setRegisterOff(String ipAddress, String port);
		
		void setRegisterOn(String ipAddress, String port);
		
		void connect(String ipAddress, String port);
		
	}
	
}
