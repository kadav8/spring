package com.example.springapp.data

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Movie {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Version
    var version: Int? = null

    @CreatedDate
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    var modifiedDate: LocalDateTime? = null

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