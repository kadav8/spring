package com.example.jdbc.dto;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class Customer {

	@Id
	private Long id;
	private String name, email;
	private Set<Reservation> reservations = new HashSet<>();

	public Customer() {
	}

	public Customer(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Customer(Long id, String name, String email, Set<Reservation> reservations) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.reservations = reservations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", reservations=" + reservations + "]";
	}
}