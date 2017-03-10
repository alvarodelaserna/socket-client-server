package com.alvarodelaserna.socket.serverclient.main.landing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.alvarodelaserna.socket.serverclient.main.client.ClientFragment;
import com.alvarodelaserna.socket.serverclient.main.server.ServerFragment;
import com.alvarodelaserna.socket.serverclient.support.base.ActivityNavigator;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragment;
import com.alvarodelaserna.socket.serverclient.support.base.BaseInteractor;
import com.alvarodelaserna.socket.serverclient.support.ui.ToastUtils;

public class LandingFragment extends BaseFragment<LandingView, BaseInteractor> {

	private static final int REQUEST_ACCESS_COARSE_LOCATION = 0;
	
	public static LandingFragment newInstance() {
		return new LandingFragment();
	}

	@Override
	protected BaseInteractor getInteractor() {
		return BaseInteractor.EMPTY_INTERACTOR;
	}

	@Override
	protected LandingView getFragmentView() {
		return new LandingView(new LandingView.ViewListener() {
			@Override
			public void onServer() {
				ServerFragment serverFragment = ServerFragment.newInstance();
				viewContextInject(ActivityNavigator.class).navigate(serverFragment);
			}

			@Override
			public void onClient() {
				ClientFragment clientFragment = ClientFragment.newInstance();
				viewContextInject(ActivityNavigator.class).navigate(clientFragment);
			}
		});
	}
	
	@Override
	protected void onViewCreated() {
		super.onViewCreated();
		requestCoarseLocationPermission();
	}
	
	private void requestCoarseLocationPermission() {
		if (ContextCompat.checkSelfPermission(viewContextInject(Context.class), Manifest
			.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[] { Manifest
								   .permission.ACCESS_COARSE_LOCATION },
							   REQUEST_ACCESS_COARSE_LOCATION);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(
		int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_ACCESS_COARSE_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
					enableButtons();
				} else {
					disableButtons();
				}
				break;
		}
	}
	
	private void enableButtons() {
		if (fragmentView != null) {
			fragmentView.enableButtons();
		}
	}
	
	private void disableButtons() {
		ToastUtils.show(viewContextInject(Context.class), "This permission is needed to access "
														  + "information from your nearest cell");
		if (fragmentView != null) {
			fragmentView.disableButtons();
		}
	}
}
