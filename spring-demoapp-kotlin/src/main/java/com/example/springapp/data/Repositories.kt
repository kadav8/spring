package com.example.springapp.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, Long>

@Repository
interface DirectorRepository : JpaRepository<Director, Long> {
    fun findOneByName(name: String): Director
}
