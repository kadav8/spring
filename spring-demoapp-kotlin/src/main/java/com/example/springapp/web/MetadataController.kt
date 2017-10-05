package com.example.springapp.web

import com.example.springapp.service.MetadataService
import com.example.springapp.types.CreateDirectorRequest
import com.example.springapp.types.CreateMovieRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class MetadataController(private val metadataService: MetadataService) {

    @PostMapping("/create/movie")
    fun postMovie(createMovieRequest: CreateMovieRequest) {
        val id = metadataService.createMovie(createMovieRequest).id
        log.info("Movie created with id: " + id)
    }

    @PostMapping("/create/director")
    fun postDirector(createDirectorRequest: CreateDirectorRequest) {
        val id = metadataService.createDirector(createDirectorRequest).id
        log.info("Director created with id: " + id)
    }

    @GetMapping("/get/{id}")
    fun getMetadata(@PathVariable("id") id: Long, hsr: HttpServletRequest): MetadataDto {
        log.info("Get metadata by id: " + id)
        val dto = metadataService.getMetadata(id)
        dto._self = hsr.requestURL.toString()
        return dto
    }

    companion object {
        private val log = LoggerFactory.getLogger(MetadataController::class.java)
    }
}