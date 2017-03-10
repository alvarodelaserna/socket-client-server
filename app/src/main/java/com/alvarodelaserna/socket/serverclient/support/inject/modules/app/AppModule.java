package com.alvarodelaserna.socket.serverclient.support.inject.modules.app;

import android.content.Context;
import com.alvarodelaserna.socket.serverclient.SocketServerClientApp;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class AppModule {

	private final SocketServerClientApp app;

	public AppModule(SocketServerClientApp app) {
		this.app = app;
	}

	@Includes
	CoreModule includeCoreModule() {
		return new CoreModule();
	}
	
	@Provides
	Context provideAppContext() {
		return app;
	}

	@Provides
	SocketServerClientApp provideUniqueIdApp() {
		return app;
	}

}
