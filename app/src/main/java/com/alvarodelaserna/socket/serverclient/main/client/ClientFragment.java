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
						
			@Override
			public void getRadio(String ipAddress, String port) {
				client = new Client(ipAddress.trim(), Integer.parseInt(port.trim()), this);
				client.execute(Request.GET_RADIO);
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
						fragmentView.enableRequestButtons();;
					}
				}
			}
			
			@Override
			public void setRegisterOff(String ipAddress, String port) {
				client = new Client(ipAddress.trim(), Integer.parseInt(port.trim()), this);
				client.execute(Request.SET_REGISTER_OFF);
			}
			
			@Override
			public void setRegisterOn(String ipAddress, String port) {
				client = new Client(ipAddress.trim(), Integer.parseInt(port.trim()), this);
				client.execute(Request.SET_REGISTER_ON);
			}
			
			@Override
			public void connect(String ipAddress, String port) {
				client = new Client(ipAddress.trim(), Integer.parseInt(port.trim()), this);
				client.execute(Request.CONNECT);
			}
			
		});
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
	}
}
