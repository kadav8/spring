package com.example.springapp.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springapp.service.MetadataService;
import com.example.springapp.types.CreateDirectorRequest;
import com.example.springapp.types.CreateMovieRequest;

@RestController
public class MetadataResource {
	private static final Logger log = LoggerFactory.getLogger(MetadataEndpoint.class);

    @Autowired
    private MetadataService metadataService;

    @PostMapping("/create/movie")
    public void postMovie(CreateMovieRequest createMovieRequest) {
        Long id = metadataService.createMovie(createMovieRequest).getId();
        log.info("Movie created with id: " + id);
    }

    @PostMapping("/create/director")
    public void postDirector(CreateDirectorRequest createDirectorRequest) {
        Long id = metadataService.createDirector(createDirectorRequest).getId();
        log.info("Director created with id: " + id);
    }

    @GetMapping("/get/{id}")
    public MetadataDto getMetadata(@PathVariable("id") Long id) {
        return metadataService.getMetadata(id);
    }

    @GetMapping("/all")
    public List<MetadataDto> getAllMetadata() {
        return metadataService.getAllMetadata();
    }
}
