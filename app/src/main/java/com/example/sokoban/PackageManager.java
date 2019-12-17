package com.example.sokoban;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PackageManager {
	private ArrayList<Package> packages;
//	private String levelStorageDir;
	private Context context;
	private PackageDownloader downloader;

	public PackageManager(Context context) {
		this.context = context;
//		levelStorageDir = Utilities.getMapFolderPath();
		update();
	}

	public void update() {
		packages = (ArrayList<Package>)DataMapper.getInstance().getPackages();

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

	public ArrayList<Package> getPackages() {
		return packages;
	}

	public void DownloadPackage(Package pckg) {
		try {
			new PackageDownloader(context).execute(pckg.getUrl(), pckg.getPath()).get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		update();
	}

	private class PackageDownloader extends AsyncTask<String, Void, Void> {
		private Context context = null;

		public PackageDownloader(Context context) {
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
