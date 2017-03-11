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
		
		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(socketServerPORT);
				while (true) {
					Socket socket = serverSocket.accept();
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
					case Request.SET_REGISTER_ON:
						setRegisterOn(socket);
						break;
					case Request.SET_REGISTER_OFF:
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
			String radioAsString = getRadioAsString();
			SendRadioToClientThread sendRadioToClientThread = new SendRadioToClientThread(socket, radioAsString);
			sendRadioToClientThread.run();
		}
		
	}
	
	void sendResponse(Socket socket, String result) {
		SendResultThread sendResultThread = new SendResultThread(socket, result);
		sendResultThread.run();
	}
	
	private class SendRadioToClientThread extends Thread {
		
		private Socket hostThreadSocket;
		private String radioData;
		
		SendRadioToClientThread(Socket socket, String radioAsString) {
			hostThreadSocket = socket;
			radioData = radioAsString;
		}
		
		@Override
		public void run() {
			OutputStream outputStream;
			try {
				outputStream = hostThreadSocket.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				printStream.print(radioData);
				printStream.close();
				message += "Sent: \n" + StringUtils.formatString(radioData) + "\n";
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
	
	private String getRadioAsString() {
		JSONObject responseBody = new JSONObject();
		try {
			JSONObject cellInfo = Connectivity.getCellInfo(context);
			responseBody.put("cellInfo", cellInfo);
			JSONObject networkInfoObj = new JSONObject();
			try {
				NetworkInfo networkInfo = Connectivity.getNetworkInfo(context);
				networkInfoObj.put("connectionType", networkInfo.getTypeName());
				networkInfoObj.put("status", networkInfo.getState());
				networkInfoObj.put("GSM", Connectivity.getGsmInfoObj(context));
				JSONObject wifiInfoObj = new JSONObject();
				wifiInfoObj.put("netName", networkInfo.getExtraInfo()
					.replace("\"", ""));
				wifiInfoObj.put("connectionSpeed", Connectivity.getConnectionSpeed(context));
				wifiInfoObj.put("isConnectedOrConnecting",
								   networkInfo.isConnectedOrConnecting());
				networkInfoObj.put("WiFi", wifiInfoObj);
				networkInfoObj.put("isRoaming", networkInfo.isRoaming());
				networkInfoObj.put("isAvailable", networkInfo.isAvailable());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			responseBody.put("networkInfo", networkInfoObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseBody.toString();
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
