package com.alvarodelaserna.socket.serverclient.main;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.alvarodelaserna.socket.serverclient.main.landing.LandingFragment;
import com.alvarodelaserna.socket.serverclient.support.base.ActivityNavigator;
import com.alvarodelaserna.socket.serverclient.support.base.BaseActivity;
import com.alvarodelaserna.socket.serverclient.support.base.BaseInteractor;

public class MainActivity extends BaseActivity<MainView, BaseInteractor> {
	
	@Override
	protected MainView getView() {
		return new MainView();
	}
	
	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}
	
	@Override
	protected void onCreated(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			navigate();
		}
	}
	
	private void navigate() {
		LandingFragment landingFragment = LandingFragment.newInstance();
		viewContextInject(ActivityNavigator.class).navigate(landingFragment,
															ActivityNavigator.B_REF_SETTINGS);
	}
	
	@Override
	protected boolean onBackIntercept() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			moveTaskToBack(true);
			return true;
		} else {
			return false;
		}
	}
}
