package com.alvarodelaserna.socket.serverclient.main.client;

import com.alvarodelaserna.socket.serverclient.support.base.BaseFragment;
import com.alvarodelaserna.socket.serverclient.support.base.BaseInteractor;

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
			public void onGetRadio(String ipAddress, String port) {
				client = new Client(ipAddress, Integer.parseInt(port), this);
				client.execute();
			}
			
			@Override
			public void onClearScreen() {
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
		});
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
	}
}
