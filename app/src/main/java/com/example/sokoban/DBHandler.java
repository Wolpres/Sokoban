package com.example.sokoban;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
	private static DBHandler instance = null;

	private DBHandler(Context context) {
		super(context, "Sokobase", null, 3);
		getWritableDatabase();
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
				"Name TEXT NOT NULL," +
				"Link TEXT NOT NULL," +
				"Is_downloaded INTEGER DEFAULT 0 NOT NULL" +
				")";

		String lvlCreationString = "CREATE TABLE Level" +
				"(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				"Name TEXT NOT NULL," +
				"Package_id INTEGER NOT NULL," +
				"FOREIGN KEY(Package_id) REFERENCES Package(ID)" +
				")";

		db.execSQL(pckgCreationString);
		db.execSQL(lvlCreationString);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Level");
		db.execSQL("DROP TABLE IF EXISTS Package");
		onCreate(db);
	}

	public static void initDB(Context context) {
		instance = new DBHandler(context.getApplicationContext());
	}

	public static DBHandler getInstance(Context context) {
		if (instance == null)
			instance = new DBHandler(context.getApplicationContext());
		return instance;
	}

	public static DBHandler getInstance() {
		if (instance == null)
			throw new RuntimeException("DB Helper instance is not bound to an object (call getInstance method with Context parameter)");
		return instance;
	}
}
