package com.example.sokoban;

public class LevelPackage {
	private String url;
	private String path;
	private String name;
	private boolean downloaded = false;

	public LevelPackage(String url, String name, boolean downloaded) {
		this.downloaded = downloaded;
		this.name = name.replace(" ", "_");
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
}
