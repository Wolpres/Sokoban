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
import java.util.List;

public class PackageAdapter extends ArrayAdapter<Package> {
	private Context context;
	private int resource;
	private List<Package> packages;
	private String levelStorageDir;
	private PackageManager lpm;

	public PackageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Package> data) {
		super(context, resource, data);

		this.context = context;
		lpm = new PackageManager(context);

		levelStorageDir = Utilities.getMapFolderPath();
		File levelStorage = new File(levelStorageDir);
		if (!levelStorage.exists())
			levelStorage.mkdir();

		this.resource = resource;
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

		final Package pckg = packages.get(position);

		holder.name.setText(pckg.getName().replace("_", " "));
		if (pckg.isDownloaded())
			holder.downloadBtn.setVisibility(View.INVISIBLE);

		holder.downloadBtn.setOnClickListener(v -> {
			lpm.DownloadPackage(pckg);
			Level[] levels = (new LevelParser(context)).quickParse(pckg);
			DataMapper.getInstance().packageDownloaded(pckg, levels);
			lpm.update();
			packages = lpm.getPackages();
			notifyDataSetChanged();
		});

		return row;
	}


	static class LevelPackageHolder implements Serializable {
		TextView name;
		ImageView downloadBtn;
	}
}
