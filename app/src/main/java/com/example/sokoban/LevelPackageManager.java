package com.example.sokoban;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LevelPackageManager {
	private ArrayList<LevelPackage> packages;
	private String levelStorageDir;

	public LevelPackageManager(Context context) {
		levelStorageDir = context.getFilesDir() + File.separator + "map_packages";
		update();
	}

	public void update() {
		packages = getDownloadedPackages();
		getOnlinePackages(packages);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			packages.sort((o1, o2) -> {
				if ((o1.isDownloaded() && o2.isDownloaded()) || (!o1.isDownloaded() && !o2.isDownloaded()))
					return 0;
				return o1.isDownloaded() ? -1 : 1;
			});
		}
	}

	public ArrayList<LevelPackage> getPackages() {
		return packages;
	}

	public String getLevelStorageDir() {
		return levelStorageDir;
	}

	private ArrayList<LevelPackage> getDownloadedPackages() {
		File dir = new File(levelStorageDir);
		File[] levels = dir.listFiles();
		ArrayList<LevelPackage> pckgs = new ArrayList<>();
		for (File level : levels)
			pckgs.add(new LevelPackage(level.getAbsolutePath(), level.getName(), true));
		return pckgs;
	}

	private void getOnlinePackages(ArrayList<LevelPackage> pckgs) {
		LevelPackage[] pckgsArr = new LevelPackage[3];
		pckgsArr[0] = new LevelPackage("http://sneezingtiger.com/sokoban/levels/sasquatch5Text.html",
				"Sasquatch V", false);
		pckgsArr[1] = new LevelPackage("http://sneezingtiger.com/sokoban/levels/minicosmosText.html",
				"Minicosmos", false);
		pckgsArr[2] = new LevelPackage("http://sneezingtiger.com/sokoban/levels/picokosmosText.html",
				"Picokosmos", false);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			List<String> names = pckgs.stream().map(LevelPackage::getName).collect(Collectors.toList());
			for (LevelPackage pckg : pckgsArr)
				if (!(names.contains(pckg.getName())))
					pckgs.add(pckg);
		}
	}
}
