package com.example.sokoban;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Adapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class LevelPackageManager {
	private ArrayList<LevelPackage> packages;
	private String levelStorageDir;
	private Context context;
	private LevelDownloader downloader;

	public LevelPackageManager(Context context) {
		this.context = context;
		levelStorageDir = context.getFilesDir() + File.separator + "map_packages";
		update();
	}

	public void update() {
		packages = getDownloadedPackages();
		getOnlinePackages(packages);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			packages.sort((o1, o2) -> {
				if ((o1.isDownloaded() && o2.isDownloaded()) || (!o1.isDownloaded() && !o2.isDownloaded())) {
					if (o1.getName().compareTo(o2.getName()) > 0)
						return 1;
					else if (o1.getName().compareTo(o2.getName()) < 0)
						return -1;
					else
						return 0;
				}
				return o1.isDownloaded() ? -1 : 1;
			});
		}
	}

	public ArrayList<LevelPackage> getPackages() {
		return packages;
	}

	public void DownloadLevel(LevelPackage pckg) {
		try {
			new LevelDownloader(context).execute(pckg.getUrl(),
					levelStorageDir + File.separator + pckg.getName()).get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		update();
	}

	public String getLevelStorageDir() {
		return levelStorageDir;
	}

	private ArrayList<LevelPackage> getDownloadedPackages() {
		File dir = new File(levelStorageDir);
		if (!dir.exists())
			dir.mkdir();
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


	private class LevelDownloader extends AsyncTask<String, Void, Void> {
		private Context context = null;

		public LevelDownloader() {

		}

		public LevelDownloader(Context context) {
			this.context = context;
		}

		@Override
		protected Void doInBackground(String... args) {
			try {
				URL url = new URL(args[0]);
				URLConnection conn = url.openConnection();
				InputStream input = new BufferedInputStream(url.openStream(), 8192);
				OutputStream output = new FileOutputStream(args[1]);
				byte data[] = new byte[1024];

				int count;
				while ((count = input.read(data)) != -1)
					output.write(data, 0, count);

				output.flush();
				output.close();
				input.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (context != null)
				Toast.makeText(context, "Level downloaded", Toast.LENGTH_SHORT).show();
		}
	}
}
