package com.example.sokoban;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SokoPlayView extends SokoDrawView {
    private Context context;
    private float touchedOnX;
    private float touchedOnY;

    public SokoPlayView(Context context) {
        super(context);
        this.context = context;
    }

    public SokoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SokoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            touchedOnX = event.getX();
            touchedOnY = event.getY();
            return true;
        }
        if (action == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (x > touchedOnX && (Math.abs(touchedOnX - x) > Math.abs(touchedOnY - y))) {
                Log.d("MySokoban", "RIGHT");
                move(0, 1);
            }
            else if (x < touchedOnX && (Math.abs(touchedOnX - x) > Math.abs(touchedOnY - y))) {
                Log.d("MySokoban", "LEFT");
                move(0, -1);
            }
            else if (y > touchedOnY && (Math.abs(touchedOnX - x) < Math.abs(touchedOnY - y))) {
                Log.d("MySokoban", "DOWN");
                move(1, 0);
            }
            else if (y < touchedOnY && (Math.abs(touchedOnX - x) < Math.abs(touchedOnY - y))) {
                Log.d("MySokoban", "UP");
                move(-1, 0);
            }
            invalidate();
            if (isWon()) {
                won();
            }
        }
        return super.onTouchEvent(event);
    }

    private void won() {
        Log.d("MySokoban", "you won");
    }

    private void move(int verDir, int horDir) {
        Coordinates pos = getPlayerPosition();
        int nextObject = map[pos.x+verDir][pos.y+horDir];
        int newBoxState;
        switch (nextObject) {
            case 0:
                map[pos.x][pos.y] = whatAfterPlayer(pos);
                map[pos.x+verDir][pos.y+horDir] = 5;
                break;
            case 1:
                break;
            case 2:
                map[pos.x][pos.y] = whatAfterPlayer(pos);
                map[pos.x+verDir][pos.y+horDir] = 6;
                break;
            case 3:
                newBoxState = boxIntoWhat(pos, verDir, horDir);
                if (newBoxState != -1) {
                    map[pos.x + 2 * verDir][pos.y + 2 * horDir] = newBoxState;
                    map[pos.x + verDir][pos.y + horDir] = 5;
                    map[pos.x][pos.y] = whatAfterPlayer(pos);
                }
                break;
            case 4:
                newBoxState = boxIntoWhat(pos, verDir, horDir);
                if (newBoxState != -1) {
                    map[pos.x + 2 * verDir][pos.y + 2 * horDir] = newBoxState;
                    map[pos.x + verDir][pos.y + horDir] = 6;
                    map[pos.x][pos.y] = whatAfterPlayer(pos);
                }
                break;
        }
    }

    private int boxIntoWhat(Coordinates pos, int verDir, int horDir) {
        int nextnextObject = map[pos.x + 2*verDir][pos.y + 2*horDir];
        if (nextnextObject == 0)
            return 3;
        else if (nextnextObject == 2)
            return 4;
        return -1;
    }

    private int whatAfterPlayer(Coordinates pos) {
        if (map[pos.x][pos.y] == 5)
            return 0;
        else if (map[pos.x][pos.y] == 6)
            return 2;
        return -1;
    }

    private Coordinates getPlayerPosition() {
        for (int i = 0 ; i < map.length ; i++) {
            for (int j = 0 ; j < map[i].length ; j++) {
                if (map[i][j] == 5 || map[i][j] == 6)
                    return new Coordinates(i, j);
            }
        }
        return null;
    }

    private boolean isWon() {
        for (int[] row : map) {
            for (int item : row) {
                if (item == 3)
                    return false;
            }
        }
        return true;
    }
}
