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

public class SokoDrawView extends View {
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

//    protected int[][] map = {
//            {1, 1, 1, 1, 1},
//            {1, 5, 3, 2, 1},
//            {1, 1, 1, 1, 1}
//    };

    protected int[][] map = {
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

    public SokoDrawView(Context context) {
        super(context);
        init(context);
    }

    public SokoDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
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
