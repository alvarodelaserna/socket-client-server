package com.alvarodelaserna.socket.serverclient.support.inject.modules.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.alvarodelaserna.socket.serverclient.SocketServerClientApp;
import com.alvarodelaserna.socket.serverclient.support.base.ActivityNavigator;
import com.alvarodelaserna.socket.serverclient.support.base.BaseActivity;
import com.alvarodelaserna.socket.serverclient.support.base.ViewNavigator;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class BaseActivityModule {

	private final BaseActivity baseActivity;
	private final ActivityNavigator navigator;

	public BaseActivityModule(BaseActivity baseActivity) {
		this.baseActivity = baseActivity;
		this.navigator = new ActivityNavigator(baseActivity);
	}

	@Provides
	public Context provideContext() {
		return baseActivity;
	}

	@Provides(named = "Application")
	public Context provideAppContext() {
		return baseActivity.getApplicationContext();
	}

	@Provides
	public Activity provideActivity() {
		return baseActivity;
	}

	@Provides
	public BaseActivity provideBaseActivity() {
		return baseActivity;
	}

	@Provides
	public Application provideApplication() {
		return baseActivity.getApplication();
	}

	@Provides
	public SocketServerClientApp provideApp() {
		return (SocketServerClientApp) baseActivity.getApplication();
	}

	@Provides
	public ActivityNavigator provideActivityNavigator() {
		return navigator;
	}

	@Provides(singleton = true)
	public ViewNavigator provideViewNavigator(BaseActivity baseActivity, ActivityNavigator navigator) {
		return new ViewNavigator(baseActivity, navigator);
	}

}
