package com.example.springapp.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Director : BaseEntity() {

    @Column(unique = true)
    var name: String? = null

    @OneToMany
    var movies: MutableList<Movie>? = null

}
