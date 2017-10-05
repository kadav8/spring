package com.example.springapp.data

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class Director {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Version
    var version: Int? = null

    @CreatedDate
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    var modifiedDate: LocalDateTime? = null

    @Column(unique = true)
    var name: String? = null

    @OneToMany
    var movies: MutableList<Movie>? = null

}
