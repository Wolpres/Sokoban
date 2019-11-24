package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException ignored){}

        Intent intent = new Intent(this, this.getClass());
        Level level = new Level();
        intent.putExtra("level", level);
        setContentView(R.layout.activity_game);
    }
//    float touchedOnX;
//    float touchedOnY;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            touchedOnX = event.getX();
//            touchedOnY = event.getY();
//        }
//        if (action == MotionEvent.ACTION_UP) {
//            float x = event.getX();
//            float y = event.getY();
//            Log.d("MySokoban", "Moved");
//            if (x > touchedOnX && (Math.abs(touchedOnX - x) > Math.abs(touchedOnY - y))) {
//                Log.d("MySokoban", "UP");
//            }
//            else if (x < touchedOnX && (Math.abs(touchedOnX - x) > Math.abs(touchedOnY - y))) {
//                Log.d("MySokoban", "DOWN");
//            }
//            else if (y > touchedOnY && (Math.abs(touchedOnX - x) < Math.abs(touchedOnY - y))) {
//                Log.d("MySokoban", "RIGHT");
//            }
//            else if (y < touchedOnY && (Math.abs(touchedOnX - x) < Math.abs(touchedOnY - y))) {
//                Log.d("MySokoban", "LEFT");
//            }
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private void redraw() {
//
//    }
}
