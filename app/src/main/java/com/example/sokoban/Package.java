package com.example.sokoban;

import java.io.Serializable;

public class Package implements Serializable {
	private int id;
	private String url;
	private String path;
	private String name;
	private boolean downloaded = false;

	public Package() {}

	public Package(int id, String name, String url, boolean downloaded) {
		this.id = id;
		this.downloaded = downloaded;
		this.name = name;
		this.url = url;
		this.path = Utilities.getMapFolderPath() + name;
	}


	public Package(String url, String name, boolean downloaded) {
		this.downloaded = downloaded;
		this.name = name;
		if (downloaded)
			this.path = url;
		else
			this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
