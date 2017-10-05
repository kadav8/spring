package com.example.springapp.service

import com.example.springapp.data.Director
import com.example.springapp.data.DirectorRepository
import com.example.springapp.data.Movie
import com.example.springapp.data.MovieRepository
import com.example.springapp.types.CreateDirectorRequest
import com.example.springapp.types.CreateMovieRequest
import com.example.springapp.web.MetadataDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
@Transactional
class MetadataService(
        private val movieRepo: MovieRepository,
        private val directorRepo: DirectorRepository) {

    fun createMovie(createMovieRequest: CreateMovieRequest): Movie {
        var movie = Movie()
        movie.title = createMovieRequest.title
        var director: Director? = directorRepo.findOneByName(createMovieRequest.directorName)
        if (director == null && !StringUtils.isEmpty(createMovieRequest.directorName)) {
            val createDirectorRequest = CreateDirectorRequest()
            createDirectorRequest.name = createMovieRequest.directorName
            director = createDirector(createDirectorRequest)
            director.movies =  mutableListOf()
        }
        if (director != null) {
            director.movies?.add(movie)
            movie.director = director
        }
        movie.year = createMovieRequest.year
        movie = movieRepo.save(movie)
        return movie
    }

    fun createDirector(createDirectorRequest: CreateDirectorRequest): Director {
        var director = Director()
        director.name = createDirectorRequest.name
        director = directorRepo.save(director)
        return director
    }

    fun getMetadata(id: Long): MetadataDto {
        var dto = MetadataDto()
        val movie = movieRepo.findOne(id)
        if (movie != null) {
            dto.movieId = id
            dto.title = movie.title
            dto.year = movie.year
            dto.movieVersion = movie.version
            dto.movieCreatedDate = movie.createdDate.toString()

            val director = movie.director
            if (director != null) {
                dto.directorId = director.id
                dto.directorName = director.name
                if (director.movies != null) {
                    dto.directorOtherMovies =
                            director.movies!!
                                .filter { m -> !m.title.equals(movie.title, ignoreCase = true) }
                                .map { m -> m.title }
                                .requireNoNulls()
                }
                dto.directorVersion = director.version
                dto.directorCreatedDate = director.createdDate.toString()
            }
        }
        return dto
    }
}