package com.example.sokoban;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
	int screenWidth, screenHeight;
	float touchDownX, touchDownY;
	MapDrawer md;
	ImageView boardImgV;
	Level level;
	Integer[][] currentMap;
	View wonScreen;

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

		// TODO move to another activity
		// load map
		md = new MapDrawer(this);
		LevelParser lp = new LevelParser(this);
		level = lp.parse()[0];
		currentMap = level.getMap();

		// get needed views (board ImageView, wonScreen include)
		boardImgV = findViewById(R.id.board);
		wonScreen = findViewById(R.id.won_screen);

		redraw();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			touchDownX = event.getX();
			touchDownY = event.getY();
			return true;
		}
		if (action == MotionEvent.ACTION_UP) {
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

			redraw();
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
				currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				currentMap[pos.x+verDir][pos.y+horDir] = 5;
				break;
			case 1:
				break;
			case 2:
				currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				currentMap[pos.x+verDir][pos.y+horDir] = 6;
				break;
			case 3:
				newBoxState = boxIntoWhat(pos, verDir, horDir);
				if (newBoxState != -1) {
					currentMap[pos.x + 2 * verDir][pos.y + 2 * horDir] = newBoxState;
					currentMap[pos.x + verDir][pos.y + horDir] = 5;
					currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				}
				break;
			case 4:
				newBoxState = boxIntoWhat(pos, verDir, horDir);
				if (newBoxState != -1) {
					currentMap[pos.x + 2 * verDir][pos.y + 2 * horDir] = newBoxState;
					currentMap[pos.x + verDir][pos.y + horDir] = 6;
					currentMap[pos.x][pos.y] = whatAfterPlayer(pos);
				}
				break;
		}
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
		Log.d("MySokoban", "you won");
		wonScreen.setVisibility(View.VISIBLE);
	}

	private void redraw() {
		boardImgV.setImageBitmap(md.draw(currentMap, screenWidth, screenHeight));
	}
}
