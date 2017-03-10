package com.alvarodelaserna.socket.serverclient.main.client;

import android.os.AsyncTask;
import com.alvarodelaserna.socket.serverclient.support.ui.Request;
import com.alvarodelaserna.socket.serverclient.support.ui.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

class Client  extends AsyncTask<String, Void, Void> {
	
	private String dstAddress;
	private int dstPort;
	private String response = "";
	private ClientView.ViewListener listener;
	
	Client(String addr, int port, ClientView.ViewListener listener) {
		dstAddress = addr;
		dstPort = port;
		this.listener = listener;
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		
		Socket socket = null;
		
		try {
			socket = new Socket(dstAddress, dstPort);
			makeRequest(socket, arg0[0]);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			response = "UnknownHostException: " + e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			response = "IOException: " + e.toString();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private void makeRequest(Socket socket, String requestName) throws IOException {
		OutputStream os = socket.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		Request request = new Request(requestName);
		oos.writeObject(request);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
			1024);
		byte[] buffer = new byte[1024];
		int bytesRead;
		InputStream inputStream = socket.getInputStream();
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, bytesRead);
			response += byteArrayOutputStream.toString("UTF-8");
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		listener.onResponseReceived(StringUtils.formatString(response));
	}
}
