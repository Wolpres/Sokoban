package com.example.sokoban;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataMapper {
	private static DataMapper instance = null;
	private SQLiteDatabase db = Sokobase.getInstance().getWritableDatabase();

	private DataMapper() {}

	public List<Package> getPackages() {
		ArrayList<Package> packages = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT ID, Name, Link, Is_downloaded FROM Package;", null);
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					do {
						packages.add(new Package(
							cursor.getInt(0),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getInt(3) == 1
						));
					} while (cursor.moveToNext());
				}
			}
			finally {
				cursor.close();
			}
		}
		return packages;
	}

	public List<Level> getLevels(Package pckg) {
		ArrayList<Level> levels = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT ID, Name, Is_done FROM Level WHERE Package_id = "+pckg.getId()+";", null);
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					do {
						levels.add(new Level(
								cursor.getInt(0),
								cursor.getString(1),
								cursor.getInt(2) == 1
						));
					} while (cursor.moveToNext());
				}
			}
			finally {
				cursor.close();
			}
		}
		return levels;
	}

	public void packageDownloaded(Package pckg, Level[] levels) {
		if (pckg.isDownloaded())
			return;
		ContentValues cvP = new ContentValues();
		cvP.put("Is_downloaded", 1);
		long result = db.update("Package", cvP, "ID = " + pckg.getId(), null);

		for (Level level : levels) {
			ContentValues cvL = new ContentValues();
			cvL.put("Name", level.getName());
			cvL.put("Package_ID", pckg.getId());
			result = db.insert("Level", null, cvL);
		}
	}

	public void levelDone(Level level) {
		level.setDone(true);
		ContentValues cv = new ContentValues();
		cv.put("is_done", 1);
		db.update("Level", cv, "ID = " + level.getId(), null);
	}

	public void reset() {
		ContentValues cv = new ContentValues();
		cv.put("Is_downloaded", 0);
		db.update("Package", cv, "", null);

		db.execSQL("DELETE FROM Level;");
	}

	public static DataMapper getInstance() {
		if (instance == null)
			instance = new DataMapper();
		return instance;
	}

	public void finalize() {
		try {
			if (db.isOpen())
				db.close();
			super.finalize();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}
