package com.example.springapp;

import com.example.springapp.service.MetadataService;
import com.example.springapp.types.CreateDirectorRequest;
import com.example.springapp.types.CreateMovieRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class LoadDatas {

    @Autowired
    private MetadataService metadataService;

    @PostConstruct
    private void load() {
        CreateDirectorRequest createDirectorRequest = new CreateDirectorRequest();
        createDirectorRequest.setName("Christopher Nolan");
        metadataService.createDirector(createDirectorRequest);

        CreateMovieRequest createMovieRequest = new CreateMovieRequest();
        createMovieRequest.setDirectorName("Christopher Nolan");
        createMovieRequest.setTitle("Interstellar");
        createMovieRequest.setYear(2014);
        metadataService.createMovie(createMovieRequest);

        createMovieRequest = new CreateMovieRequest();
        createMovieRequest.setDirectorName("Christopher Nolan");
        createMovieRequest.setTitle("Dunkirk");
        createMovieRequest.setYear(2017);
        metadataService.createMovie(createMovieRequest);

        createMovieRequest = new CreateMovieRequest();
        createMovieRequest.setDirectorName("Alfonso Cuaron");
        createMovieRequest.setTitle("Harry Potter and the Prisoner of Azkaban");
        createMovieRequest.setYear(2004);
        metadataService.createMovie(createMovieRequest);
    }
}
