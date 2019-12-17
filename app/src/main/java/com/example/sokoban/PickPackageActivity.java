package com.example.sokoban;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class PickPackageActivity extends AppCompatActivity {
	PackageAdapter pa;
	ListView lv;
	LevelParser lp;
	PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_level_package);

		pm = new PackageManager(this);

		lp = new LevelParser(this);
		lv = findViewById(R.id.package_picker);
		pa = new PackageAdapter(this, R.layout.pick_package_item_layout, pm.getPackages());
		lv.setAdapter(pa);
		lv.setOnItemClickListener((parent, view, position, id) -> {
			pm.update();
			Package pckg = pm.getPackages().get(position);
			if (pckg.isDownloaded()) {
				Intent intent = new Intent(PickPackageActivity.this, PickLevelActivity.class);
				intent.putExtra("level_package", pckg);
				startActivityForResult(intent, 1);
			}
			else {
				Toast.makeText(this, "Download package before playing", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
