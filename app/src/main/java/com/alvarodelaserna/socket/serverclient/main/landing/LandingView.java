package com.alvarodelaserna.socket.serverclient.main.landing;

import android.view.View;
import android.widget.Button;
import com.alvarodelaserna.socket.serverclient.R;
import com.alvarodelaserna.socket.serverclient.support.base.BaseFragmentView;

class LandingView extends BaseFragmentView {

	private final ViewListener viewListener;
	private Button serverButton, clientButton;
	
	LandingView(ViewListener viewListener) {
		super(R.layout.landing_layout);
		this.viewListener = viewListener;
	}

	@Override
	protected void setUpView(View view) {
		serverButton = (Button) view.findViewById(R.id.landing_layout_button_server);
		clientButton = (Button) view.findViewById(R.id.landing_layout_button_client);
		serverButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onServer();
			}
		});
		clientButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onClient();
			}
		});
	}
	
	void disableButtons() {
		serverButton.setEnabled(false);
		clientButton.setEnabled(false);
	}
	
	void enableButtons() {
		serverButton.setEnabled(true);
		clientButton.setEnabled(true);
	}
	
	interface ViewListener {

		void onServer();

		void onClient();

	}

}
