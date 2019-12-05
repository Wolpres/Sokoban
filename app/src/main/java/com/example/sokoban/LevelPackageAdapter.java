package com.example.sokoban;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LevelPackageAdapter extends ArrayAdapter<LevelPackage> {
	private Context context;
	private int resource;
	private List<LevelPackage> packages;
	private String levelStorageDir;

	public LevelPackageAdapter(@NonNull Context context, int resource, List<LevelPackage> data) {
		super(context, resource, data);


		this.context = context;

		levelStorageDir = context.getFilesDir() + "/" + "map_packages";
		File levelStorage = new File(levelStorageDir);
		if (!levelStorage.exists())
			levelStorage.mkdir();

		this.resource = resource;
		data.addAll(getDownloadedPackages());
		Collections.addAll(data, getOnlinePackages());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			data.sort(new Comparator<LevelPackage>() {
				@Override
				public int compare(LevelPackage o1, LevelPackage o2) {
					if ((o1.isDownloaded() && o2.isDownloaded()) || (!o1.isDownloaded() && !o2.isDownloaded()))
						return 0;
					return o1.isDownloaded() ? -1 : 1;
				}
			});
		}
		this.packages = data;
	}

	// FIXME is not called
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View row = convertView;
		final LevelPackageHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false);
			holder = new LevelPackageHolder();
			holder.name = row.findViewById(R.id.package_name);
			holder.downloadBtn = row.findViewById(R.id.download_btn);

			row.setTag(holder);
		}
		else {
			holder = (LevelPackageHolder) row.getTag();
		}

		final LevelPackage pckg = packages.get(position);

		holder.name.setText(pckg.getName().replace("_", " "));
		if (pckg.isDownloaded())
			holder.downloadBtn.setVisibility(View.INVISIBLE);

		holder.downloadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new LevelDownloader(context).execute(pckg.getUrl(),
						levelStorageDir + File.separator + pckg.getName());
			}
		});

		return row;
	}

	private ArrayList<LevelPackage> getDownloadedPackages() {
		File dir = new File(levelStorageDir);
		File[] levels = dir.listFiles();
		ArrayList<LevelPackage> pckgs = new ArrayList<>();
		for (File level : levels)
			pckgs.add(new LevelPackage(level.getPath(), level.getName(), true));
		return pckgs;
	}

	private LevelPackage[] getOnlinePackages() {
		LevelPackage[] pckgs = new LevelPackage[3];

		pckgs[0] = new LevelPackage("http://sneezingtiger.com/sokoban/levels/sasquatch5Text.html",
				"Sasquatch V", false);
		pckgs[1] = new LevelPackage("http://sneezingtiger.com/sokoban/levels/minicosmosText.html",
				"Minicosmos", false);
		pckgs[2] = new LevelPackage("http://sneezingtiger.com/sokoban/levels/picokosmosText.html",
				"Picokosmos", false);

		return pckgs;
	}

	static class LevelPackageHolder implements Serializable {
		TextView name;
		ImageView downloadBtn;
	}
}
