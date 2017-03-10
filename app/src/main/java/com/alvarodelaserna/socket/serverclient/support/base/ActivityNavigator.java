package com.alvarodelaserna.socket.serverclient.support.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.LinkedList;

public final class ActivityNavigator {

	private static final NavSettings DF_SETTINGS = new NaveSettingsBuilder().isAnimated(true)
		.build();
	public static final NavSettings B_REF_SETTINGS = new NaveSettingsBuilder().clearStack(true)
		.isReference(true)
		.build();
	public static final NavSettings B_REF_NO_CLEAR_STACK_SETTINGS = new NaveSettingsBuilder().isReference(true)
		.build();
	private static final String REF_NAME = "REF";

	private final int container;
	private final FragmentManager fragmentManager;
	private final LinkedList<Integer> refs;
	private int fCount;

	public ActivityNavigator(BaseActivity activity) {
		fragmentManager = activity.getSupportFragmentManager();
		this.container = activity.getFrameContainer();
		this.refs = new LinkedList<>();
		this.fCount = 0;
	}

	public void navigate(Fragment fragment) {
		navigate(fragment, DF_SETTINGS);
	}

	public void navigate(Fragment fragment, NavSettings settings) {
		if (settings.clearStack) {
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			refs.clear();
			fCount = 0;
		}
		fCount++;
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (settings.isAnimated) {
			transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		}
		if (settings.isReference) {
			refs.add(fCount);
		}
		transaction.addToBackStack(settings.isReference ? REF_NAME : null);
		transaction.replace(container, fragment, String.valueOf(fCount))
			.commitAllowingStateLoss();
	}

	public boolean navigateBack() {
		boolean onBackEnabled = fCount > 1;
		if (onBackEnabled) {
			fragmentManager.popBackStack();
			if (!refs.isEmpty() && refs.getLast() == fCount) {
				refs.removeLast();
			}
			fCount--;
		}
		return onBackEnabled;
	}

	public boolean navigateUp() {
		boolean onUpEnabled = !(refs.isEmpty() || refs.size() == 1 && refs.getLast() == fCount);
		if (onUpEnabled) {
			fragmentManager.popBackStack(REF_NAME, 0);
			if (refs.getLast() == fCount) {
				refs.removeLast();
			}
			fCount = refs.getLast();
		}
		return onUpEnabled;
	}

	@Nullable
	public Fragment getActiveFragment() {
		String tag = String.valueOf(fCount);
		return fragmentManager.findFragmentByTag(tag);
	}

	int getFragmentCount() {
		return fCount;
	}

	String saveInstanceState() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(fCount)
			.append('&');
		for (int i = 0; i < refs.size(); i++) {
			if (i > 0) {
				stringBuilder.append('_');
			}
			stringBuilder.append(refs.get(i));
		}
		return stringBuilder.toString();
	}

	void restoreInstanceState(String savedInstance) {
		if (savedInstance != null) {
			String[] values = savedInstance.split("&");
			fCount = Integer.valueOf(values[0]);
			if (values.length == 2) {
				for (String ref : values[1].split("_")) {
					refs.add(Integer.valueOf(ref));
				}
			}
		}
	}

	public static class NavSettings {

		final boolean clearStack;
		final boolean isReference;
		final boolean isAnimated;

		private NavSettings(NaveSettingsBuilder builder) {
			this.clearStack = builder.clearStack;
			this.isReference = builder.isReference;
			this.isAnimated = builder.isAnimated;
		}

		public NaveSettingsBuilder toBuild() {
			return new NaveSettingsBuilder().clearStack(clearStack)
				.isReference(isReference)
				.isAnimated(isAnimated);
		}

	}

	public static class NaveSettingsBuilder {

		private boolean clearStack;
		private boolean isReference;
		private boolean isAnimated;

		public NaveSettingsBuilder clearStack(boolean clearStack) {
			this.clearStack = clearStack;
			return this;
		}

		public NaveSettingsBuilder isReference(boolean isReference) {
			this.isReference = isReference;
			return this;
		}

		public NaveSettingsBuilder isAnimated(boolean animated) {
			isAnimated = animated;
			return this;
		}

		public NavSettings build() {
			return new NavSettings(this);
		}

	}

}
