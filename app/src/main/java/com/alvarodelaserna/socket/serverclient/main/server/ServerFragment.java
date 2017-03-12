package com.alvarodelaserna.socket.serverclient.main.server;

import android.content.Context;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragment;
import com.alvarodelaserna.socket.serverclient.support.base.BaseInteractor;
import com.alvarodelaserna.socket.serverclient.support.ui.Connectivity;
import java.net.Socket;

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
		return new ServerView(new ServerView.ViewListener() {
			@Override
			public void onGetRadio() {
				if (server != null && fragmentView != null) {
					String radioData = server.getRadio();
					fragmentView.paintGetRadio(radioData);
				}
			}
		});
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
			
			@Override
			public void setRegisterOff(Socket socket) {
				switchNetworkState(socket, false);
			}
			
			@Override
			public void setRegisterOn(Socket socket) {
				switchNetworkState(socket, true);
			}
			
			@Override
			public void connect(Socket socket) {
				makeConnection(socket);
			}
		});
		if (fragmentView != null) {
			fragmentView.updateIpAddress(server.getIpAddress() + ":" + server.getPort());
		}
	}
	
	private void makeConnection(Socket socket) {
		if (server != null) {
			server.sendResponse(socket, "CONNECTED");
		}
	}
	
	private void switchNetworkState(Socket socket, boolean enabled) {
		String result = "OK";
		try {
			Connectivity.setMobileDataEnabled(viewContextInject(Context.class), enabled);
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}
		if (server != null) {
			server.sendResponse(socket, result);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		server.onDestroy();
	}
	
	public interface Listener {
		
		void onMessageSent(String message);
		
		void onMessageUpdate(String message);
		
		void setRegisterOff(Socket socket);
		
		void setRegisterOn(Socket socket);
		
		void connect(Socket socket);
	}
}
