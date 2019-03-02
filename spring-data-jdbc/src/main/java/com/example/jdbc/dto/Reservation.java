package com.example.jdbc.dto;

import org.springframework.data.annotation.Id;

public class Reservation {

	@Id
	private Long id;
	private String sku;

	public Reservation(Long id, String sku) {
		this.id = id;
		this.sku = sku;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", sku=" + sku + "]";
	}
}
