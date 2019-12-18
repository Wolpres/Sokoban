package com.example.sokoban;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
	private int screenWidth, screenHeight;
	private float touchDownX, touchDownY;
	private MapDrawer md;
	private ImageView boardImgV;
	private Level level;
	private Integer[][] currentMap;
	private View wonScreen, pauseScreen;
	private int HISTORY_CAPACITY = 10;
	private ArrayList<Integer[][]> history = new ArrayList<>(HISTORY_CAPACITY);
	private boolean won = false;
	private boolean forbidMove = false;

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// get display metrics
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;

		// load map
		md = new MapDrawer(this);
		level = (Level) getIntent().getSerializableExtra("level");
		currentMap = level.getMapClone();

		// get needed views (board ImageView, wonScreen include)
		boardImgV = findViewById(R.id.board);
		initWonScreen();
		initPauseScreen();
		initBackButton();

		redraw();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (forbidMove)
			return super.onTouchEvent(event);
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			touchDownX = event.getX();
			touchDownY = event.getY();
			return true;
		}
		if (action == MotionEvent.ACTION_UP) {
			Log.d("MySokoban", "want to move");
			float x = event.getX();
			float y = event.getY();
			if (x > touchDownX && (Math.abs(touchDownX - x) > Math.abs(touchDownY - y))) {
				Log.d("MySokoban", "RIGHT");
				move(0, 1);
			}
			else if (x < touchDownX && (Math.abs(touchDownX - x) > Math.abs(touchDownY - y))) {
				Log.d("MySokoban", "LEFT");
				move(0, -1);
			}
			else if (y > touchDownY && (Math.abs(touchDownX - x) < Math.abs(touchDownY - y))) {
				Log.d("MySokoban", "DOWN");
				move(1, 0);
			}
			else if (y < touchDownY && (Math.abs(touchDownX - x) < Math.abs(touchDownY - y))) {
				Log.d("MySokoban", "UP");
				move(-1, 0);
			}

			Log.d("MySokoban", "moved");
			redraw();
			Log.d("MySokoban", "drawn");
			if (isWon()) {
				won();
			}
		}
		return super.onTouchEvent(event);
	}

	private void move(int verDir, int horDir) {
		Coordinates pos = getPlayerPosition();
		int nextObject = currentMap[pos.x+verDir][pos.y+horDir];
		int newBoxState;
		switch (nextObject) {
			case 0:
				notifyHistory();
				currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				currentMap[pos.x+verDir][pos.y+horDir] = 5;
				break;
			case 1:
				break;
			case 2:
				notifyHistory();
				currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				currentMap[pos.x+verDir][pos.y+horDir] = 6;
				break;
			case 3:
				newBoxState = boxIntoWhat(pos, verDir, horDir);
				if (newBoxState != -1) {
					notifyHistory();
					currentMap[pos.x + 2 * verDir][pos.y + 2 * horDir] = newBoxState;
					currentMap[pos.x + verDir][pos.y + horDir] = 5;
					currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				}
				break;
			case 4:
				newBoxState = boxIntoWhat(pos, verDir, horDir);
				if (newBoxState != -1) {
					notifyHistory();
					currentMap[pos.x + 2 * verDir][pos.y + 2 * horDir] = newBoxState;
					currentMap[pos.x + verDir][pos.y + horDir] = 6;
					currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				}
				break;
		}
	}

	private void notifyHistory() {
		Integer[][] tmpMap = new Integer[currentMap.length][];
		for (int i = 0 ; i < currentMap.length ; i++)
			tmpMap[i] = currentMap[i].clone();

		history.add(tmpMap);
		if (history.size() > HISTORY_CAPACITY)
			history.remove(0);
	}

	private void goBack() {
		if (history.size() > 0) {
			currentMap = history.get(history.size() - 1);
			history.remove(history.size() - 1);
			redraw();
		}
		else
			Toast.makeText(this, "No more moves available", Toast.LENGTH_SHORT).show();
	}

	private int boxIntoWhat(Coordinates pos, int verDir, int horDir) {
		int nextnextObject = currentMap[pos.x + 2*verDir][pos.y + 2*horDir];
		if (nextnextObject == 0)
			return 3;
		else if (nextnextObject == 2)
			return 4;
		return -1;
	}

	private int whatAfterPlayer(Coordinates pos) {
		if (currentMap[pos.x][pos.y] == 5)
			return 0;
		else if (currentMap[pos.x][pos.y] == 6)
			return 2;
		return -1;
	}

	private Coordinates getPlayerPosition() {
		for (int i = 0 ; i < currentMap.length ; i++) {
			for (int j = 0 ; j < currentMap[i].length ; j++) {
				if (currentMap[i][j] == 5 || currentMap[i][j] == 6)
					return new Coordinates(i, j);
			}
		}
		return null;
	}

	private boolean isWon() {
		for (Integer[] row : currentMap) {
			for (int item : row) {
				if (item == 3)
					return false;
			}
		}
		return true;
	}

	private void won() {
		won = true;
		forbidMove = true;
		wonScreen.setVisibility(View.VISIBLE);
	}

	private void initWonScreen() {
		wonScreen = findViewById(R.id.won_screen);
		findViewById(R.id.playAgainBtn).setOnClickListener(v -> reset());
		findViewById(R.id.nextLevelBtn).setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.putExtra("option", 1);
			intent.putExtra("won", won);
			setResult(RESULT_OK, intent);
			finish();
		});
		findViewById(R.id.mainMenuBtn).setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.putExtra("option", 2);
			intent.putExtra("won", won);
			setResult(RESULT_OK, intent);
			finish();
		});
	}

	private void initPauseScreen() {
		pauseScreen = findViewById(R.id.pause_screen);
		findViewById(R.id.pause_button).setOnClickListener(v -> {
			boardImgV.setImageBitmap(md.draw(new Integer[][] {{0}}, screenWidth, screenHeight));
			forbidMove = true;
			pauseScreen.setVisibility(View.VISIBLE);
		});
		findViewById(R.id.resume_btn).setOnClickListener(v -> {
			pauseScreen.setVisibility(View.INVISIBLE);
			forbidMove = false;
			redraw();
		});
		findViewById(R.id.settings_btn).setOnClickListener(v -> {

		});
		findViewById(R.id.main_menu_btn).setOnClickListener(v -> {
			Intent intent = new Intent();
			intent.putExtra("opt", 2);
			intent.putExtra("won", false);
			setResult(RESULT_OK, intent);
			finish();
		});
		findViewById(R.id.restart_level_btn).setOnClickListener(v -> reset());
	}

	private void initBackButton() {
		ImageView backButton = findViewById(R.id.back_button);
		backButton.setOnClickListener(v -> goBack());
	}

	private void reset() {
		forbidMove = false;
		wonScreen.setVisibility(View.INVISIBLE);
		pauseScreen.setVisibility(View.INVISIBLE);
		currentMap = level.getMapClone();
		history = new ArrayList<>();
		redraw();
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
			saveMap(outState, currentMap, 0);

		new Thread(() -> saveHistory(outState)).start();

		super.onSaveInstanceState(outState);
	}

	private void saveHistory(Bundle bundle) {
		bundle.putInt("history_size", history.size());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			for (int i = 0 ; i < history.size() ; i++) {
				saveMap(bundle, history.get(i), i+1);
			}
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	private void saveMap(Bundle bundle, Integer[][] map, int code) {
		bundle.putInt("length " + code, map.length);
		for (int i = 0 ; i < map.length ; i++)
			bundle.putIntArray(code + " " + i, Arrays.stream(map[i]).mapToInt(n -> n).toArray());
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			currentMap = loadMap(savedInstanceState, 0);
			redraw();
			new Thread(() -> loadHistory(savedInstanceState)).start();
		}
	}

	private void loadHistory(Bundle bundle) {
		int n = bundle.getInt("history_size");
		for (int i = 0 ; i < n ; i++)
			history.add(loadMap(bundle, i+1));
	}

	private Integer[][] loadMap(Bundle bundle, int code) {
		int n = bundle.getInt("length " + code);
		Integer[][] map = new Integer[n][];

		for (int i = 0 ; i < n ; i++) {
			int[] tmpArr = bundle.getIntArray(code + " " + i);
			map[i] = new Integer[tmpArr.length];
			for (int j = 0 ; j < tmpArr.length ; j++)
				map[i][j] = tmpArr[j];
		}
		return map;
	}

	private void redraw() {
		boardImgV.setImageBitmap(md.draw(currentMap, screenWidth, screenHeight));
	}
}
