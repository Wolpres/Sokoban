package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Utilities.initUtils(getApplicationContext());

		findViewById(R.id.play_btn).setOnClickListener(v -> startActivity(
				new Intent(MainActivity.this, PickPackageActivity.class)));
		findViewById(R.id.settings_btn).setOnClickListener(v -> startActivity(
				new Intent(MainActivity.this, SettingsActivity.class)));
	}
}
