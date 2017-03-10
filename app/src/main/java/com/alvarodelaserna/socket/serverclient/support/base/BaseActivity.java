package com.alvarodelaserna.socket.serverclient.support.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.alvarodelaserna.socket.serverclient.instruments.inject.AppInjector;
import com.alvarodelaserna.socket.serverclient.instruments.inject.Injector;
import com.alvarodelaserna.socket.serverclient.support.inject.KinjectInjector;
import com.alvarodelaserna.socket.serverclient.support.inject.modules.activity.BaseActivityModule;

public abstract class BaseActivity<V extends BaseActivityView, I extends BaseInteractor> extends AppCompatActivity {

	private static final String BUNDLE_SAVE_NAVIGATOR = "SAVE_NAVIGATOR";

	protected V view;
	protected I interactor;
	Injector appInjector;
	Injector viewContextInjector;
	private boolean isResumed;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		view = getView();
		interactor = getInteractor();
		appInjector = AppInjector.getInjector();
		viewContextInjector = new KinjectInjector(getActivityModule());
		restoreState(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(view.layoutId);
		view.setUpViewContextInjector(viewContextInjector);
		view.setUpView(findViewById(android.R.id.content));
		Bundle arguments = getIntent().getExtras();
		if (arguments != null) {
			loadArguments(arguments);
		}
		onCreated(savedInstanceState);
	}

	protected abstract V getView();

	protected abstract I getInteractor();

	protected Object getActivityModule() {
		return new BaseActivityModule(this);
	}

	private void restoreState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			String restoreState = savedInstanceState.getString(BUNDLE_SAVE_NAVIGATOR);
			viewContextInject(ActivityNavigator.class).restoreInstanceState(restoreState);
		}
	}

	protected void loadArguments(Bundle arguments) {
	}

	protected void onCreated(Bundle savedInstanceState) {
	}

	@Override
	protected void onStart() {
		super.onStart();
		view.onViewFocusChange(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isResumed = true;
	}

	@Override
	@CallSuper
	protected void onPause() {
		super.onPause();
		isResumed = false;
		interactor.cancelPendingOperations();
	}

	@Override
	protected void onStop() {
		super.onStop();
		view.onViewFocusChange(false);
	}

	@Override
	public final void onBackPressed() {
		if (!view.onBackIntercept()) {
			ActivityNavigator activityNavigator = viewContextInject(ActivityNavigator.class);
			Fragment activeFragment = activityNavigator.getActiveFragment();
			if (activeFragment != null && activeFragment instanceof BaseFragment) {
				BaseFragment fragment = (BaseFragment) activeFragment;
				if (fragment.onBackPressed()) {
					return;
				}
			}
			if (!(onBackIntercept() || activityNavigator.navigateBack() || onFinishTransitionIntercept())) {
				supportFinishAfterTransition();
			}
		}
	}

	protected boolean onBackIntercept() {
		return false;
	}

	protected boolean onFinishTransitionIntercept() {
		return false;
	}

	protected void setUp(ViewNavigator.NavConf navConf) {
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		String saveInstanceState = viewContextInject(ActivityNavigator.class).saveInstanceState();
		outState.putString(BUNDLE_SAVE_NAVIGATOR, saveInstanceState);
	}

	public boolean isBaseResumed() {
		return isResumed;
	}

	protected final <T> T appInject(Class<T> tClass) {
		return appInjector.inject(tClass);
	}

	protected final <T> T viewContextInject(Class<T> tClass) {
		return viewContextInjector.inject(tClass);
	}

	final int getFrameContainer() {
		return view.containerId;
	}

}
