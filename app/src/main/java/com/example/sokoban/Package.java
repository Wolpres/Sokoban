package com.example.sokoban;

import java.io.Serializable;

public class Package implements Serializable {
	private int id;
	private String url;
	private String name;
	private boolean downloaded = false;

	public Package() {}

	public Package(int id, String name, String url, boolean downloaded) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.downloaded = downloaded;
	}


	public Package(String name, String url) {
		this(0, name, url, false);
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getPath() {
		return Utilities.getMapFolderPath() + getName();
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
