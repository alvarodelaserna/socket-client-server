package com.alvarodelaserna.socket.serverclient.support.base;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alvarodelaserna.socket.serverclient.instruments.inject.Injector;

public class BaseFragmentView {

	final ViewVisibilityHint visibilityHint;
	private final int layoutId;
	private Injector viewContextInjector;
	View view;

	protected BaseFragmentView(@LayoutRes int layoutId) {
		this.layoutId = layoutId;
		visibilityHint = new ViewVisibilityHint();
	}

	void setUp(Injector viewContextInjector) {
		this.viewContextInjector = viewContextInjector;
	}

	void setUpFragmentView(LayoutInflater inflater, ViewGroup container) {
		this.view = inflater.inflate(layoutId, container, false);
		setUpView(view);
	}

	protected void setUpView(View view) {
	}

	boolean onBackIntercept() {
		return false;
	}

	protected <T> T viewContextInject(Class<T> tClass) {
		return viewContextInjector.inject(tClass);
	}

}
