package com.example.sokoban;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SokoView extends View {
    /*
    0 - empty
    1 - wall
    2 - target
    3 - box_nok
    4 - box_ok
    5 - player
    6 - player on goal
     */
    private Bitmap playerImg = BitmapFactory.decodeResource(getResources(), R.drawable.player_down);
    private Bitmap[] bitmap;
    private int width;
    private int height;
    private int size;
    private int maxLength;
    private int xOffset;
    private int yOffset;
    private float touchedOnX;
    private float touchedOnY;

    private int[][] map = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 3, 2, 2, 3, 1, 0, 1},
            {1, 0, 1, 2, 3, 2, 3, 0, 1},
            {1, 0, 3, 2, 2, 3, 5, 0, 1},
            {1, 0, 1, 2, 3, 2, 3, 0, 1},
            {1, 0, 3, 2, 2, 3, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    public SokoView(Context context) {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        maxLength = maxLength();

        bitmap = new Bitmap[7];
        bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.floor);
        bitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.block);
        bitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.target);
        bitmap[3] = BitmapFactory.decodeResource(getResources(), R.drawable.box_nok);
        bitmap[4] = BitmapFactory.decodeResource(getResources(), R.drawable.box_ok);
        bitmap[5] = playerImg;
        bitmap[6] = playerImg;
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
        }
        return super.onTouchEvent(event);
    }

    public void move(int verDir, int horDir) {
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

    public int boxIntoWhat(Coordinates pos, int verDir, int horDir) {
        int nextnextObject = map[pos.x + 2*verDir][pos.y + 2*horDir];
        if (nextnextObject == 0)
            return 3;
        else if (nextnextObject == 2)
            return 4;
        return -1;
    }

    public int whatAfterPlayer(Coordinates pos) {
        if (map[pos.x][pos.y] == 5)
            return 0;
        else if (map[pos.x][pos.y] == 6)
            return 2;
        return -1;
    }

    public Coordinates getPlayerPosition() {
        for (int i = 0 ; i < map.length ; i++) {
            for (int j = 0 ; j < map[i].length ; j++) {
                if (map[i][j] == 5 || map[i][j] == 6)
                    return new Coordinates(i, j);
            }
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / maxLength;
        height = h / map.length;

        if (width > height)
            size = height;
        else
            size = width;

        xOffset = (w - (size * maxLength))/2;
        yOffset = (h - (size * map.length))/2;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0 ; i < map.length ; i++) {
            for (int j = 0 ; j < map[i].length ; j++) {
                if (map[i][j] == 6)
                    canvas.drawBitmap(bitmap[2], null, new Rect(j*size + xOffset, i*size + yOffset, (j+1)*size + xOffset, (i+1)*size + yOffset), null);
                if (map[i][j] != 0)
                    canvas.drawBitmap(bitmap[map[i][j]], null, new Rect(j*size + xOffset, i*size + yOffset, (j+1)*size + xOffset, (i+1)*size + yOffset), null);
            }
        }
    }

    private int maxLength() {
        int max = 0;
        for (int[] ints : map) {
            if (ints.length > max)
                max = ints.length;
        }
        return max;
    }
}
