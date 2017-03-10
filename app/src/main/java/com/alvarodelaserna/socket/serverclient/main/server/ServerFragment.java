package com.alvarodelaserna.socket.serverclient.main.server;

import android.content.Context;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragment;
import com.alvarodelaserna.socket.serverclient.support.base.BaseInteractor;

public class ServerFragment extends BaseFragment<ServerView, BaseInteractor> {
	
	private Server server;
	
	public static ServerFragment newInstance() {
		return new ServerFragment();
	}
	
	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}
	
	@Override
	protected ServerView getFragmentView() {
		return new ServerView();
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
		server = new Server(viewContextInject(Context.class), new Listener() {
			
			@Override
			public void onMessageSent(String message) {
				if (fragmentView != null) {
					fragmentView.updateMessage(message);
				}
			}
			
			@Override
			public void onMessageUpdate(String message) {
				if (fragmentView != null) {
					fragmentView.updateMessage(message);
				}
			}
		});
		if (fragmentView != null) {
			fragmentView.updateIpAddress(server.getIpAddress() + ":" + server.getPort());
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		server.onDestroy();
	}
	
	interface Listener {
		
		void onMessageSent(String message);
		
		void onMessageUpdate(String message);
	}
}
