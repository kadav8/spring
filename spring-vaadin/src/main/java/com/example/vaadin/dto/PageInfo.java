package com.example.vaadin.dto;

public class PageInfo {

	private final String link;
	private final String title;

	public PageInfo(String link, String title) {
		this.link = link;
		this.title = title;
	}

	public String getLink() {
		return link;
	}
	public String getTitle() {
		return title;
	}
}
