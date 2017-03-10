package com.alvarodelaserna.socket.serverclient.support.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.v4.app.ActivityOptionsCompat.makeCustomAnimation;

public final class AppNavigator {

	public static final AppNavSetting DEFAULT = new AppNavSettingsBuilder().build();
	public static final AppNavSetting NEW_TASK = new AppNavSettingsBuilder().flags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK)
		.build();

	private AppNavigator() {
	}

	public static <T> void navigateTo(Class<T> activityClass, Context context) {
		navigateTo(activityClass, context, DEFAULT);
	}

	public static <T> void navigateTo(Class<T> activityClass, Context context, AppNavSetting navSetting) {
		Intent intent = new Intent(context, activityClass);
		if (navSetting.extras != null) {
			intent.putExtras(navSetting.extras);
		}
		if (navSetting.flags != null) {
			intent.addFlags(navSetting.flags);
		}
		Bundle bundle = navSetting.animated ? makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle() : null;
		context.startActivity(intent, bundle);
	}

	public static <T> void navigateToForResult(Class<T> activityClass, int reqCode, Activity context, AppNavSetting navSetting) {
		Intent intent = new Intent(context, activityClass);
		if (navSetting.extras != null) {
			intent.putExtras(navSetting.extras);
		}
		if (navSetting.flags != null) {
			intent.addFlags(navSetting.flags);
		}
		Bundle bundle = navSetting.animated ? makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle() : null;
		context.startActivityForResult(intent, reqCode, bundle);
	}

	public static class AppNavSetting {

		public final Bundle extras;
		public final Integer flags;
		public final boolean animated;

		private AppNavSetting(AppNavSettingsBuilder builder) {
			this.extras = builder.extras;
			this.flags = builder.flags;
			this.animated = builder.animated;
		}

		public AppNavSettingsBuilder toBuild() {
			return new AppNavSettingsBuilder().extras(extras)
				.flags(flags)
				.animated(animated);
		}

	}

	public static class AppNavSettingsBuilder {

		private Bundle extras;
		private Integer flags;
		private boolean animated = true;

		public AppNavSettingsBuilder extras(Bundle extras) {
			this.extras = extras;
			return this;
		}

		public AppNavSettingsBuilder flags(Integer flags) {
			this.flags = flags;
			return this;
		}

		public AppNavSettingsBuilder animated(boolean animated) {
			this.animated = animated;
			return this;
		}

		public AppNavSetting build() {
			return new AppNavSetting(this);
		}

	}

}
