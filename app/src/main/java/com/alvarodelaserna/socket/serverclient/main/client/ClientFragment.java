package com.alvarodelaserna.socket.serverclient.main.client;

import com.alvarodelaserna.socket.serverclient.support.base.BaseFragment;
import com.alvarodelaserna.socket.serverclient.support.base.BaseInteractor;
import com.alvarodelaserna.socket.serverclient.support.ui.Request;

public class ClientFragment extends BaseFragment<ClientView, BaseInteractor> {
	
	private Client client;
	
	public static ClientFragment newInstance() {
		return new ClientFragment();
	}
	
	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}
	
	@Override
	protected ClientView getFragmentView() {
		return new ClientView(new ClientView.ViewListener() {
			
			/**
			 * Creates a new client request
			 * @param ipAddress {@link String String} representing the IP address of the server
			 * @param port {@link Integer int} representing the server port
			 */
			@Override
			public void makeRequest(String ipAddress, int port, String requestName) {
				client = new Client(ipAddress, port, this);
				client.execute(requestName);
			}
			
			@Override
			public void clearScreen() {
				if (fragmentView != null) {
					fragmentView.clearMessage();
				}
			}
			
			@Override
			public void onResponseReceived(String response) {
				if (fragmentView != null) {
					if (!response.equals("CONNECTED")) {
						fragmentView.updateMessageReceived(response);
					} else {
						fragmentView.enableRequestButtons();
						;
					}
				}
			}
			
			@Override
			public void getRadio(String ipAddress, String port) {
				makeRequest(ipAddress.trim(), Integer.parseInt(port.trim()), Request.GET_RADIO);
			}
			
			@Override
			public void setRegisterOff(String ipAddress, String port) {
				makeRequest(ipAddress.trim(), Integer.parseInt(port.trim()),
							Request.SET_REGISTER_OFF);
			}
			
			@Override
			public void setRegisterOn(String ipAddress, String port) {
				makeRequest(ipAddress.trim(), Integer.parseInt(port.trim()),
							Request.SET_REGISTER_ON);
			}
			
			@Override
			public void connect(String ipAddress, String port) {
				makeRequest(ipAddress.trim(), Integer.parseInt(port.trim()), Request.CONNECT);
			}
			
		});
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
	}
}
