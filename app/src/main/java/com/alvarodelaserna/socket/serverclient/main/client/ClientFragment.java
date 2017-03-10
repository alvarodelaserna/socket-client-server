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
					fragmentView.updateMessageReceived(response);
				}
			}
			
			@Override
			public void turnOffNetwork(String ipAddress, String port) {
				client = new Client(ipAddress.trim(), Integer.parseInt(port.trim()), this);
				client.execute(Request.TURN_OFF_NETWORK);
			}
			
			@Override
			public void turnOnNetwork(String ipAddress, String port) {
				client = new Client(ipAddress.trim(), Integer.parseInt(port.trim()), this);
				client.execute(Request.TURN_ON_NETWORK);
			}
		});
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
	}
}
