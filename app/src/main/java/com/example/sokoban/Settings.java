package com.example.sokoban;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Settings {
	private static Context context = Utilities.getAppContext();
	private static final String PACKAGE_NAME = "com.example.sokoban";
//	private static SharedPreferences prefs = context.getDefaultSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
	private static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Utilities.getAppContext());

	public static String getConnectionMethod() {
		return prefs.getString("connection", "none");
	}
}
