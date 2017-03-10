package com.alvarodelaserna.socket.serverclient.support.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewVisibilityHint {

	private static final int STATE_INIT = -1;
	private static final int STATE_VISIBLE = 1;
	private static final int STATE_HIDDEN = 2;
	private static final int STATE_RELEASED = 3;

	private CopyOnWriteArrayList<VisibilityListener> listeners;
	private int state = STATE_INIT;

	public void addViewHintListener(VisibilityListener hintListener) {
		if (hintListener != null) {
			getListeners().add(hintListener);
			if (state == STATE_VISIBLE) {
				hintListener.onParentViewVisible();
			} else if (state == STATE_RELEASED) {
				hintListener.onParentViewReleased();
			}
		}
	}

	public void removeViewHintListener(VisibilityListener listener) {
		getListeners().remove(listener);
	}

	void onViewVisible() {
		state = STATE_VISIBLE;
		for (VisibilityListener listener : getListeners()) {
			listener.onParentViewVisible();
		}
	}

	void onViewHidden() {
		state = STATE_HIDDEN;
		for (VisibilityListener listener : getListeners()) {
			listener.onParentViewHidden();
		}
	}

	void onViewRelease() {
		state = STATE_RELEASED;
		for (VisibilityListener listener : getListeners()) {
			listener.onParentViewReleased();
		}
	}

	private List<VisibilityListener> getListeners() {
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<>();
		}
		return listeners;
	}

	public interface VisibilityListener {

		void onParentViewVisible();

		void onParentViewHidden();

		void onParentViewReleased();

	}

}
