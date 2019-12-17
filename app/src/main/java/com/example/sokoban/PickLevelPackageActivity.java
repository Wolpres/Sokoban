package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class PickLevelPackageActivity extends AppCompatActivity {
	LevelPackageAdapter lpa;
	ListView lv;
	LevelParser lp;
	LevelPackageManager lpm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_level_package);

		lpm = new LevelPackageManager(this);

		lp = new LevelParser(this);
		lv = findViewById(R.id.level_package_picker);
		lpa = new LevelPackageAdapter(this, R.layout.pick_level_package_item_layout, lpm.getPackages());
		lv.setAdapter(lpa);
		lv.setOnItemClickListener((parent, view, position, id) -> {
			lpm.update();
			LevelPackage pckg = lpm.getPackages().get(position);
			if (pckg.isDownloaded()) {
				Intent intent = new Intent(PickLevelPackageActivity.this, PickLevelActivity.class);
				intent.putExtra("level_package", pckg);
				startActivityForResult(intent, 1);
			}
			else {
				Toast.makeText(this, "Download package before playing", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
