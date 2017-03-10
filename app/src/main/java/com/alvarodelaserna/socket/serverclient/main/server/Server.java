package com.alvarodelaserna.socket.serverclient.main.server;

import android.content.Context;
import android.net.NetworkInfo;
import com.alvarodelaserna.socket.serverclient.main.MainActivity;
import com.alvarodelaserna.socket.serverclient.support.ui.Connectivity;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {
	
	private final Context context;
	private MainActivity activity;
	private ServerSocket serverSocket;
	private String message = "";
	private static final int socketServerPORT = 8080;
	private ServerFragment.Listener listener;
	
	public Server(Context context, ServerFragment.Listener viewListener) {
		this.context = context;
		activity = (MainActivity) context;
		listener = viewListener;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}
	
	int getPort() {
		return socketServerPORT;
	}
	
	void onDestroy() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class SocketServerThread extends Thread {
		
		int count = 0;
		
		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(socketServerPORT);
				
				while (true) {
					Socket socket = serverSocket.accept();
					count++;
					message += "Received request from "
							   + socket.getInetAddress() + ":"
							   + socket.getPort() + "\n";
					
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							listener.onMessageUpdate(message);
						}
					});
					
					SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
						socket, count);
					socketServerReplyThread.run();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class SocketServerReplyThread extends Thread {
		
		private Socket hostThreadSocket;
		int cnt;
		
		SocketServerReplyThread(Socket socket, int c) {
			hostThreadSocket = socket;
			cnt = c;
		}
		
		@Override
		public void run() {
			OutputStream outputStream;
			JSONObject responseBody = new JSONObject();
			try {
				JSONObject cellInfo = Connectivity.getCellInfo(context);
				cellInfo.put("connectionSpeed", Connectivity.getConnectionSpeed(context));
				responseBody.put("cell", cellInfo);
				JSONObject networkInfoObj = Connectivity.getNetworkInfoObj(context);
				try {
					NetworkInfo networkInfo = Connectivity.getNetworkInfo(context);
					networkInfoObj.put("status", networkInfo.getState());
					networkInfoObj.put("netName", networkInfo.getExtraInfo().replace("\"", ""));
					networkInfoObj.put("type", networkInfo.getTypeName());
					networkInfoObj.put("isRoaming", networkInfo.isRoaming());
					networkInfoObj.put("isAvailable", networkInfo.isAvailable());
					networkInfoObj.put("isConnectedOrConnecting", networkInfo.isConnectedOrConnecting());
					networkInfoObj.put("isFailover", networkInfo.isFailover());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				responseBody.put("network", networkInfoObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			String msgReply = responseBody.toString();
			
			try {
				outputStream = hostThreadSocket.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				printStream.print(msgReply);
				printStream.close();
				
				message += "Sent: \n" + msgReply + "\n";
				
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						listener.onMessageSent(message);
					}
				});
				
			} catch (IOException e) {
				e.printStackTrace();
				message += "Something wrong! " + e.toString() + "\n";
			}
			
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					listener.onMessageUpdate(message);
				}
			});
		}
		
	}
	
	String getIpAddress() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
				.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
					.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
					.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress
						.nextElement();
					
					if (inetAddress.isSiteLocalAddress()) {
						ip += "Server running at : "
							  + inetAddress.getHostAddress();
					}
				}
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}
		return ip;
	}
}
