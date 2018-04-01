package com.example.springapp.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.springapp.aspect.LatencyLogger;
import com.example.springapp.data.Director;
import com.example.springapp.data.DirectorRepository;
import com.example.springapp.data.Movie;
import com.example.springapp.data.MovieRepository;
import com.example.springapp.types.CreateDirectorRequest;
import com.example.springapp.types.CreateMovieRequest;
import com.example.springapp.web.MetadataDto;

@Service
@Transactional
public class MetadataService {

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private DirectorRepository directorRepo;

    @LatencyLogger
    public Movie createMovie(final CreateMovieRequest createMovieRequest) {
        Movie movie = new Movie();
        movie.setTitle(createMovieRequest.getTitle());
        Director director = directorRepo.findOneByName(createMovieRequest.getDirectorName());
        if(director == null && !StringUtils.isEmpty(createMovieRequest.getDirectorName())) {
            CreateDirectorRequest createDirectorRequest = new CreateDirectorRequest();
            createDirectorRequest.setName(createMovieRequest.getDirectorName());
            director = createDirector(createDirectorRequest);
            director.setMovies(new ArrayList<>());
        }
        if(director != null) {
            director.getMovies().add(movie);
            movie.setDirector(director);
        }
        movie.setYear(createMovieRequest.getYear());
        movie = movieRepo.save(movie);
        return movie;
    }

    @LatencyLogger
    public Director createDirector(final CreateDirectorRequest createDirectorRequest) {
        Director director = new Director();
        director.setName(createDirectorRequest.getName());
        director = directorRepo.save(director);
        return director;
    }

    public MetadataDto getMetadata(final Long id) {
        MetadataDto dto = null;
        Optional<Movie> movieOpt = movieRepo.findById(id);
        if (movieOpt.isPresent()) {
        	Movie movie = movieOpt.get();
            dto = new MetadataDto();
            dto.setMovieId(id);
            dto.setTitle(movie.getTitle());
            dto.setYear(movie.getYear());
            dto.setMovieVersion(movie.getVersion());
            dto.setMovieCreatedDate(movie.getCreatedDate().toString());

            Director director = movie.getDirector();
            if (director != null) {
                dto.setDirectorId(movie.getDirector().getId());
                dto.setDirectorName(movie.getDirector().getName());
                if (director.getMovies() != null) {
                    dto.setDirectorOtherMovies(
                            director.getMovies()
                                    .stream()
                                    .filter(m -> !m.getTitle().equalsIgnoreCase(movie.getTitle()))
                                    .map(m -> m.getTitle()).collect(Collectors.toList()));
                }
                dto.setDirectorVersion(director.getVersion());
                dto.setDirectorCreatedDate(director.getCreatedDate().toString());
            }
        }
        return dto;
    }
}
