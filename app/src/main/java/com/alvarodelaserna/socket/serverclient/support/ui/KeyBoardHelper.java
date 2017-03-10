package com.alvarodelaserna.socket.serverclient.support.ui;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class KeyBoardHelper {

	public static void hideKeyBoard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
