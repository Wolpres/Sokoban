package com.example.sokoban;

import android.content.Context;
import android.view.inputmethod.ExtractedTextRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LevelParser {
	Context context;

	public LevelParser(Context context) {
		this.context = context;
	}

	public Level[] parse(Package pckg) {

		String content = null;
		try {
			content = readStream(new FileInputStream(pckg.getPath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String> levelsStr = new ArrayList<>(Arrays.asList(content.split("\n\n")));
		List<Level> levels = DataMapper.getInstance().getLevels(pckg);


		for (int i = 0, j = 0 ; i < levelsStr.size() ; i++) {
			try {
				Integer[][] map = mapParser(levelsStr.get(i));
				levels.get(j++).setMap(map);
			}
			catch (Exception e) {
				// continue
			}
		}
		return levels.toArray(new Level[0]);
	}

	private Integer[][] mapParser(String str) throws Exception {
		ArrayList<Integer[]> mapList = new ArrayList<>();
		String[] lines = str.split("\n");

		for (int i = 0 ; i < lines.length ; i++) {
			try {
				mapList.add(parseLine(lines[i]));
			}
			catch (Exception e) {
				// continue
			}
		}
		if (mapList.size() == 0)
			throw new Exception();
		return mapList.toArray(new Integer[mapList.size()][]);
	}

	private Integer[] parseLine(String line) throws Exception {
		Integer[] row = new Integer[line.length()];
		if (row.length == 0)
			throw new Exception();
		char[] cArr = line.toCharArray();
		for (int i = 0 ; i < cArr.length ; i++) {
			int item = mapper(cArr[i]);
			if (item != -1)
				row[i] = item;
			else
				throw new Exception();
		}
		return row;
	}

	public Level[] quickParse(Package pckg) {
		String content = null;
		try {
			content = readStream(new FileInputStream(pckg.getPath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] levelsStr = content.split("\n\n");
		int ctr = 0;
		for (int i = 0 ; i < levelsStr.length ; i++) {
			try {
				mapParser(levelsStr[i]);
				ctr++;
			}
			catch (Exception e) {
				// continue
			}
		}

		Level[] levels = new Level[ctr];
		for (int i = 0 ; i < levels.length ; i++) {
			levels[i] = new Level(0, context.getResources().getString(R.string.level) + " " + (i+1), false);
		}

		return levels;
	}

	private static String readStream(InputStream is) {
		StringBuilder sb = new StringBuilder(512);
		try {
			Reader r = new InputStreamReader(is, "UTF-8");
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	private int mapper(char c) {
		switch (c) {
			case ' ':
				return 0;
			case '#':
				return 1;
			case '.':
				return 2;
			case '$':
				return 3;
			case '*':
				return 4;
			case '@':
				return 5;
			case '+':
				return 6;
			default:
				return -1;
		}
	}
}
