package com.alvarodelaserna.socket.serverclient.support.base;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import com.alvarodelaserna.socket.serverclient.instruments.inject.Injector;

public class BaseActivityView {

	final int layoutId;
	final int containerId;
	private Injector viewContextInjector;

	protected BaseActivityView(@LayoutRes int layoutId, @IdRes int containerId) {
		this.layoutId = layoutId;
		this.containerId = containerId;
	}

	void setUpViewContextInjector(Injector viewContextInjector) {
		this.viewContextInjector = viewContextInjector;
	}

	protected void setUpView(View view) {
	}

	void onViewFocusChange(boolean hasFocus) {
	}

	protected boolean onBackIntercept() {
		return false;
	}

	protected final <T> T viewContextInject(Class<T> tClass) {
		if (viewContextInjector == null) {
			throw new IllegalStateException("Injection request in uninitialized injector, maybe in the constructor.");
		}
		return viewContextInjector.inject(tClass);
	}

}
