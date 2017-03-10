package com.alvarodelaserna.socket.serverclient;

import android.app.Application;
import com.alvarodelaserna.socket.serverclient.support.inject.KinjectInjector;
import com.alvarodelaserna.socket.serverclient.support.inject.modules.app.AppModule;

import static com.alvarodelaserna.socket.serverclient.instruments.inject.AppInjector.initInjector;

public class SocketServerClientApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		setUp();

	}

	private void setUp() {
		setUpInjector();
	}

	private void setUpInjector() {
		AppModule appModule = new AppModule(this);
		initInjector(new KinjectInjector(appModule));
	}

}
