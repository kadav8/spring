package com.example.springapp.data

import javax.persistence.*

@Entity
class Movie : BaseEntity() {

    var title: String? = null

    @ManyToOne
    var director: Director? = null

    var year: Int? = null

    @ElementCollection
    @JoinTable(name = "movie_properties", joinColumns = arrayOf(JoinColumn(name = "id")))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    var properties: MutableMap<String, String>? = null

}