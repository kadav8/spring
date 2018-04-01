package com.example.springapp.web;

import java.util.List;

public class MetadataDto {

    private Long movieId;
    private String title;
    private Integer year;
    private Long directorId;
    private String directorName;
    private List<String> directorOtherMovies;
    private int movieVersion;
    private String movieCreatedDate;
    private int directorVersion;
    private String directorCreatedDate;
    private String _self;
	public Long getMovieId() {
		return movieId;
	}
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Long getDirectorId() {
		return directorId;
	}
	public void setDirectorId(Long directorId) {
		this.directorId = directorId;
	}
	public String getDirectorName() {
		return directorName;
	}
	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}
	public List<String> getDirectorOtherMovies() {
		return directorOtherMovies;
	}
	public void setDirectorOtherMovies(List<String> directorOtherMovies) {
		this.directorOtherMovies = directorOtherMovies;
	}
	public int getMovieVersion() {
		return movieVersion;
	}
	public void setMovieVersion(int movieVersion) {
		this.movieVersion = movieVersion;
	}
	public String getMovieCreatedDate() {
		return movieCreatedDate;
	}
	public void setMovieCreatedDate(String movieCreatedDate) {
		this.movieCreatedDate = movieCreatedDate;
	}
	public int getDirectorVersion() {
		return directorVersion;
	}
	public void setDirectorVersion(int directorVersion) {
		this.directorVersion = directorVersion;
	}
	public String getDirectorCreatedDate() {
		return directorCreatedDate;
	}
	public void setDirectorCreatedDate(String directorCreatedDate) {
		this.directorCreatedDate = directorCreatedDate;
	}
	public String get_self() {
		return _self;
	}
	public void set_self(String _self) {
		this._self = _self;
	}
}
