package com.alvarodelaserna.socket.serverclient.main.server;

import android.content.Context;
import android.net.NetworkInfo;
import com.alvarodelaserna.socket.serverclient.main.MainActivity;
import com.alvarodelaserna.socket.serverclient.support.ui.Connectivity;
import com.alvarodelaserna.socket.serverclient.support.ui.Request;
import com.alvarodelaserna.socket.serverclient.support.ui.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
	
	Server(Context context, ServerFragment.Listener viewListener) {
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
					parseSocket(socket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void parseSocket(Socket socket) throws IOException {
			if (!StringUtils.isNullOrEmpty(message)) {
				message = "";
			}
			message += "Received request from " + socket.getInetAddress() + ":" + socket.getPort() + "\n";
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			Request to = null;
			try {
				to = (Request) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					listener.onMessageUpdate(message);
				}
			});
			if (to != null) {
				message += "Request: " + to.value + "\n";
				switch (to.value) {
					case Request.GET_RADIO:
						getRadio(socket);
						break;
					case Request.TURN_ON_NETWORK:
						setRegisterOn(socket);
						break;
					case Request.TURN_OFF_NETWORK:
						setRegisterOff(socket);
						break;
					default:
						connect(socket);
						break;
				}
			}
		}
		
		private void connect(Socket socket) {
			if (listener != null) {
				listener.connect(socket);
			}
		}
		
		private void setRegisterOn(Socket socket) {
			if (listener != null) {
				listener.setRegisterOn(socket);
			}
		}
		
		private void setRegisterOff(Socket socket) {
			if (listener != null) {
				listener.setRegisterOff(socket);
			}
		}
		
		private void getRadio(Socket socket) {
			GetRadioThread getRadioThread = new GetRadioThread(socket, count);
			getRadioThread.run();
		}
		
	}
	
	void sendResponse(Socket socket, String result) {
		SendResultThread sendResultThread = new SendResultThread(socket, result);
		sendResultThread.run();
	}
	
	private class GetRadioThread extends Thread {
		
		private Socket hostThreadSocket;
		int cnt;
		
		GetRadioThread(Socket socket, int c) {
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
					networkInfoObj.put("netName", networkInfo.getExtraInfo()
						.replace("\"", ""));
					networkInfoObj.put("type", networkInfo.getTypeName());
					networkInfoObj.put("isRoaming", networkInfo.isRoaming());
					networkInfoObj.put("isAvailable", networkInfo.isAvailable());
					networkInfoObj.put("isConnectedOrConnecting",
									   networkInfo.isConnectedOrConnecting());
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
				message += "Sent: \n" + StringUtils.formatString(msgReply) + "\n";
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						listener.onMessageSent(message);
					}
				});
				
			} catch (IOException e) {
				e.printStackTrace();
				message += "Something went wrong! " + e.toString() + "\n";
			}
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					listener.onMessageUpdate(message);
				}
			});
		}
		
	}
	
	private class SendResultThread extends Thread {
		
		private Socket hostThreadSocket;
		private String result;
		
		SendResultThread(Socket socket, String result) {
			hostThreadSocket = socket;
			this.result = result;
		}
		
		@Override
		public void run() {
			OutputStream outputStream;
			
			String msgReply = result;
			try {
				outputStream = hostThreadSocket.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				printStream.print(msgReply);
				printStream.close();
				message += "Sent: \n" + StringUtils.formatString(msgReply) + "\n";
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						listener.onMessageSent(message);
					}
				});
				
			} catch (IOException e) {
				e.printStackTrace();
				message += "Something went wrong! " + e.toString() + "\n";
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
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();
					if (inetAddress.isSiteLocalAddress()) {
						ip += "Server running at : " + inetAddress.getHostAddress();
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
