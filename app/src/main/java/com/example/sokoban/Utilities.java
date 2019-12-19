package com.example.sokoban;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;

public class Utilities {
	private static String mapFolderPath;
	private static Context appContext;
	private static ConnectivityManager connMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	private static NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

	public static void initUtils(Context context) {
		appContext = context;
		mapFolderPath = context.getFilesDir() + File.separator + "map_packages" + File.separator;
	}

	public static Context getAppContext() {
		return appContext;
	}

	public static String getMapFolderPath() {
		return mapFolderPath;
	}

	private static boolean isMobileConnected() {
		if (activeInfo != null && activeInfo.isConnected())
			return activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
		return false;
	}

	private static boolean isWifiConnected() {
		if (activeInfo != null && activeInfo.isConnected())
			return activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
		return false;
	}
}
