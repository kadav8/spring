package com.example.springapp

import com.example.springapp.service.MetadataService
import com.example.springapp.types.CreateDirectorRequest
import com.example.springapp.types.CreateMovieRequest
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaAuditing
class DemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoApplication::class.java, *args)
}

@Service
class LoadDatas(private val metadataService: MetadataService) {
    @PostConstruct
    fun load() {
        val createDirectorRequest = CreateDirectorRequest()
        createDirectorRequest.name = "Christopher Nolan"
        metadataService.createDirector(createDirectorRequest)

        var createMovieRequest = CreateMovieRequest()
        createMovieRequest.directorName = "Christopher Nolan"
        createMovieRequest.title = "Interstellar"
        createMovieRequest.year = 2014
        metadataService.createMovie(createMovieRequest)

        createMovieRequest = CreateMovieRequest()
        createMovieRequest.directorName = "Christopher Nolan"
        createMovieRequest.title = "Dunkirk"
        createMovieRequest.year = 2017
        metadataService.createMovie(createMovieRequest)

        createMovieRequest = CreateMovieRequest()
        createMovieRequest.directorName = "Alfonso Cuaron"
        createMovieRequest.title = "Harry Potter and the Prisoner of Azkaban"
        createMovieRequest.year = 2004
        metadataService.createMovie(createMovieRequest)
    }
}