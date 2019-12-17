package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.play_btn).setOnClickListener(v -> startActivity(
				new Intent(MainActivity.this, PickLevelPackageActivity.class)));

		DBHandler.initDB(this);
	}
}
