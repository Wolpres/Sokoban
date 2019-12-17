package com.example.sokoban;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Sokobase extends SQLiteOpenHelper {
	private static Sokobase instance = null;

	private Sokobase(Context context) {
		super(context, "Sokobase", null, 3);
	}

	@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String pckgCreationString = "CREATE TABLE Package" +
				"(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"Name TEXT UNIQUE NOT NULL," +
				"Link TEXT NOT NULL," +
				"Is_downloaded INTEGER DEFAULT 0 NOT NULL" +
				")";

		String lvlCreationString = "CREATE TABLE Level" +
				"(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"Name TEXT NOT NULL," +
				"Package_id INTEGER NOT NULL," +
				"Is_done INTEGER DEFAULT 0 NOT NULL," +
				"FOREIGN KEY(Package_id) REFERENCES Package(ID)" +
				")";

		db.execSQL(pckgCreationString);
		db.execSQL(lvlCreationString);

		Package[] packages = getDefaultPackages();
		for (Package pckg : packages) {
			ContentValues values = new ContentValues();
			values.put("Name", pckg.getName());
			values.put("Link", pckg.getUrl());
			long result = db.insert("Package", null, values);
			if (result == -1)
				Log.d("MySokoban", "WTF");
		}
	}

	private Package[] getDefaultPackages() {
		Package[] packages = new Package[3];
		packages[0] = new Package("http://sneezingtiger.com/sokoban/levels/sasquatch5Text.html",
				"Sasquatch V", false);
		packages[1] = new Package("http://sneezingtiger.com/sokoban/levels/minicosmosText.html",
				"Minicosmos", false);
		packages[2] = new Package("http://sneezingtiger.com/sokoban/levels/picokosmosText.html",
				"Picokosmos", false);

		return packages;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Level");
		db.execSQL("DROP TABLE IF EXISTS Package");
		onCreate(db);
	}

	public static void initDB(Context context) {

		instance = new Sokobase(context.getApplicationContext());
	}

	public static Sokobase getInstance(Context context) {
		if (instance == null)
			instance = new Sokobase(context.getApplicationContext());
		return instance;
	}

	public static Sokobase getInstance() {
		if (instance == null)
			throw new RuntimeException("DB Helper instance is not bound to an object (call getInstance method with Context parameter)");
		return instance;
	}
}
