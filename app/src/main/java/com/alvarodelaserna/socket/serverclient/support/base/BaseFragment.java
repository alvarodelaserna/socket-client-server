package com.alvarodelaserna.socket.serverclient.support.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alvarodelaserna.socket.serverclient.instruments.inject.Injector;

public abstract class BaseFragment<V extends BaseFragmentView, I extends BaseInteractor> extends Fragment {

	protected V fragmentView;
	protected I interactor;
	private String fTag;
	private Injector appInjector;
	private Injector viewContextInjector;

	@Override
	public final void onAttach(Context context) {
		super.onAttach(context);
		appInjector = ((BaseActivity) context).appInjector;
		viewContextInjector = ((BaseActivity) context).viewContextInjector;
	}

	@Override
	@CallSuper
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			loadArguments(arguments);
		}
		interactor = getInteractor();
		fTag = "F" + interactor.tag;
	}

	protected void loadArguments(Bundle arguments) {
	}

	protected abstract I getInteractor();

	@Nullable
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = getFragmentView();
		fragmentView.setUp(viewContextInjector);
		fragmentView.setUpFragmentView(inflater, container);
		return fragmentView.view;
	}

	protected abstract V getFragmentView();

	@Override
	public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onViewCreated();
	}

	protected void onViewCreated() {
	}

	@Override
	@CallSuper
	public void onStart() {
		super.onStart();
		getVisibilityHint().onViewVisible();
	}

	@Override
	@CallSuper
	public void onStop() {
		super.onStop();
		getVisibilityHint().onViewHidden();
	}

	@Override
	@CallSuper
	public void onDestroyView() {
		super.onDestroyView();
		interactor.cancelPendingOperations();
		getVisibilityHint().onViewRelease();
		fragmentView = null;
	}

	private ViewVisibilityHint getVisibilityHint() {
		return fragmentView.visibilityHint;
	}

	@Override
	@CallSuper
	public void onDestroy() {
		super.onDestroy();
		interactor.cancelPendingOperations(fTag);
	}

	final boolean onBackPressed() {
		return fragmentView.onBackIntercept() || onBackIntercept();
	}

	protected boolean onBackIntercept() {
		return false;
	}

	protected final <T> T appInject(Class<T> tClass) {
		return appInjector.inject(tClass);
	}

	protected final <T> T viewContextInject(Class<T> tClass) {
		return viewContextInjector.inject(tClass);
	}

}
