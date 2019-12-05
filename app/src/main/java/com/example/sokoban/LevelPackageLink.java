package com.example.sokoban;

public class LevelPackageLink {
	private String url;
	private String name;

	public LevelPackageLink(String url, String name) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
}
