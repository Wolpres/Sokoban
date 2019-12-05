package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

public class PickLevelPackageActivity extends AppCompatActivity {
	LevelPackageAdapter lpa;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_level_package);

		ArrayList<LevelPackage> pckgs = new ArrayList<>();

		lv = findViewById(R.id.level_package_picker);
		lpa = new LevelPackageAdapter(this, R.layout.pick_level_package_item_layout, pckgs);
		lv.setAdapter(lpa);
	}
}
