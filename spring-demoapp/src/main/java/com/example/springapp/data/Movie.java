package com.example.springapp.data;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;

@Entity
public class Movie extends AbstractEntity {

    private String title;

    @ManyToOne
    private Director director;

    private Integer year;

    @ElementCollection
    @JoinTable(name="movie_properties", joinColumns=@JoinColumn(name="id"))
    @MapKeyColumn (name="key")
    @Column(name="value")
    private Map<String,String> properties;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Director getDirector() {
		return director;
	}

	public void setDirector(Director director) {
		this.director = director;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
