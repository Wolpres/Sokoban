package com.example.sokoban;

import android.content.Context;
import android.content.Intent;
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

import java.io.Serializable;

public class LevelAdapter extends ArrayAdapter<Level> {
	Context context;
	int resource;
	Level[] levels;
	MapDrawer drawer;


	public LevelAdapter(@NonNull Context context, int resource, @NonNull Level[] data) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.levels = data;
		drawer = new MapDrawer(context);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View row = convertView;
		LevelHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false); // FIXME
			holder = new LevelHolder();
			holder.name = row.findViewById(R.id.level_name_tv);
			holder.preview = row.findViewById(R.id.level_preview);

			row.setTag(holder);
		}
		else {
			holder = (LevelHolder) row.getTag();
		}

		Level level = levels[position];

		holder.name.setText(level.getName());
		holder.preview.setImageBitmap(drawer.draw(level.getMap(), 150, 150));

		return row;
	}

	static class LevelHolder implements Serializable {
		TextView name;
		ImageView preview;
	}
}
