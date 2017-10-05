package com.example.springapp.web

data class MetadataDto (
        var movieId: Long? = null,
        var title: String? = null,
        var year: Int? = null,
        var directorId: Long? = null,
        var directorName: String? = null,
        var directorOtherMovies: List<String>? = null,
        var movieVersion: Int? = null,
        var movieCreatedDate: String? = null,
        var directorVersion: Int? = null,
        var directorCreatedDate: String? = null,
        var _self: String? = null)