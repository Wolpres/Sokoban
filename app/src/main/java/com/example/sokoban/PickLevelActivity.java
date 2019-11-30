package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class PickLevelActivity extends AppCompatActivity {
	private Level[] levels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		levels = new LevelParser(getApplicationContext()).parse();

		setContentView(R.layout.activity_pick_level);
		ListView lv = findViewById(R.id.level_picker);
		LevelAdapter la = new LevelAdapter(this, R.layout.pick_level_item_layout, levels);
		lv.setAdapter(la);
	}
}
