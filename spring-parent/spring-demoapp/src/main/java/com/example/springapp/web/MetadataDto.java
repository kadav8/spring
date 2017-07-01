package com.example.springapp.web;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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

}
