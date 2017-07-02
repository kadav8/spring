package com.example.springapp.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@EntityListeners(DirectorListener.class)
public class Director extends AbstractEntity {

    @Column(unique = true)
    private String name;

    @OneToMany
    private List<Movie> movies;

}
