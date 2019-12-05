package com.example.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class MapDrawer {
    /*
    0 - empty
    1 - wall
    2 - target
    3 - box_nok
    4 - box_ok
    5 - player
    6 - player on goal
     */
    private Bitmap[] tiles;
    private int tileWidthPx, tileHeightPx;
    private int size;
    private int maxLength;
    private int xOffset, yOffset;
    private Bitmap playerImg;
    private Context context;

    private Integer[][] map;

    public MapDrawer(Context context) {
        this.context = context;
        playerImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_down);

        tiles = new Bitmap[7];
        tiles[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor);
        tiles[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.block);
        tiles[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.target);
        tiles[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.box_nok);
        tiles[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.box_ok);
        tiles[5] = playerImg;
        tiles[6] = playerImg;
    }

    public Bitmap draw(Integer[][] map, int viewWidthPx, int viewHeightPx) {
        this.map = map;
        maxLength = maxLength(map);

        calcSize(viewWidthPx, viewHeightPx);
        calcOffset(viewWidthPx, viewHeightPx);

        Bitmap bmp = Bitmap.createBitmap(viewWidthPx, viewHeightPx, Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        for (int i = 0 ; i < map.length ; i++) {
            for (int j = 0 ; j < map[i].length ; j++) {
                if (map[i][j] == 6)
                    canvas.drawBitmap(tiles[2], null, new Rect(j*size + xOffset,
                            i*size + yOffset, (j+1)*size + xOffset,
                            (i+1)*size + yOffset), null);
                if (map[i][j] != 0)
                    canvas.drawBitmap(tiles[map[i][j]], null, new Rect(j*size + xOffset,
                            i*size + yOffset, (j+1)*size + xOffset,
                            (i+1)*size + yOffset), null);
            }
        }
        return bmp;
    }

    private void calcSize(int w, int h) {
        tileWidthPx = w / maxLength;
        tileHeightPx = h / map.length;

        if (tileWidthPx > tileHeightPx)
            size = tileHeightPx;
        else
            size = tileWidthPx;
    }

    private void calcOffset(int w, int h) {
        xOffset = (w - (size * maxLength))/2;
        yOffset = (h - (size * map.length))/2;
    }

    private int maxLength(Integer[][] map) {
        int max = 0;
        for (Integer[] ints : map) {
            if (ints.length > max)
                max = ints.length;
        }
        return max;
    }
}
