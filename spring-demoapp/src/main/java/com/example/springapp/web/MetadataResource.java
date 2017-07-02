package com.example.springapp.web;

import com.example.springapp.service.MetadataService;
import com.example.springapp.types.CreateDirectorRequest;
import com.example.springapp.types.CreateMovieRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class MetadataResource {

    @Autowired
    private MetadataService metadataService;

    @Value("${myexample.msg}")
    private String msg;

    @Value("${myexample.msg2:SPRING}")
    private String msg2;

    @GetMapping("/hello")
    public String hello() {
        return msg + " " + msg2;
    }

    @PostMapping("/create/movie")
    public void postMovie(CreateMovieRequest createMovieRequest) {
        Long id = metadataService.createMovie(createMovieRequest).getId();
        log.info("Movie created with id: " + id);
    }

    @PostMapping("/create/director")
    public void postMovie(CreateDirectorRequest createDirectorRequest) {
        Long id = metadataService.createDirector(createDirectorRequest).getId();
        log.info("Director created with id: " + id);
    }

    @GetMapping("/get/{id}")
    public MetadataDto getMetadata(@PathVariable("id") Long id, HttpServletRequest hsr) {
        log.info("Get metadata by id: " + id);
        MetadataDto dto = metadataService.getMetadata(id);
        dto.set_self(hsr.getRequestURL().toString());
        return dto;
    }
}
