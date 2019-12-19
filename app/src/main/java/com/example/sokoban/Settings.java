package com.example.sokoban;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	private static Context context = Utilities.getAppContext();
	private static final String PACKAGE_NAME = "com.example.app";
	private static SharedPreferences prefs = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

	public static String getConnectionMethod() {
		return prefs.getString("connection", "none");
	}
}
