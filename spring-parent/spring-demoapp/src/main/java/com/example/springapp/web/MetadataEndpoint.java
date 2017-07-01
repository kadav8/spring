package com.example.springapp.web;

import com.example.springapp.service.MetadataService;
import com.example.springapp.types.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class MetadataEndpoint {
    public static final String NAMESPACE_URI = "http://types.springapp.example.com";

    @Autowired
    private MetadataService metadataService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createMovieRequest")
    @ResponsePayload
    public CreateMovieResponse createMovie(@RequestPayload CreateMovieRequest createMovieRequest) {
        CreateMovieResponse response = new CreateMovieResponse();
        try {
            Long id = metadataService.createMovie(createMovieRequest).getId();
            response.setResult("Movie created with id: " + id);
            response.setErrorCode(0);
            log.info(response.getResult());
        } catch(Exception e) {
            response.setResult("Movie creating failed: " + e.getMessage());
            response.setErrorCode(1);
            log.error(response.getResult(), e);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createDirectorRequest")
    @ResponsePayload
    public CreateDirectorResponse createDirector(@RequestPayload CreateDirectorRequest createDirectorRequest) {
        CreateDirectorResponse response = new CreateDirectorResponse();
        try {
            Long id = metadataService.createDirector(createDirectorRequest).getId();
            response.setResult("Director created with id: " + id);
            response.setErrorCode(0);
            log.info(response.getResult());
        } catch(Exception e) {
            response.setResult("Director creating failed: " + e.getMessage());
            response.setErrorCode(1);
            log.error(response.getResult(), e);
        }
        return response;
    }
}
