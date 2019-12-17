package com.example.sokoban;

import android.content.Context;

import java.io.File;

public class Utilities {
	private static String mapFolderPath;
	private static Context appContext;

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
}
