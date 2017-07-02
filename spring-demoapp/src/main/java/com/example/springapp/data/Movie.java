package com.example.springapp.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter @Setter
public class Movie extends AbstractEntity {

    private String title;

    @ManyToOne
    private Director director;

    private Integer year;

    @ElementCollection
    @JoinTable(name="movie_properties", joinColumns=@JoinColumn(name="id"))
    @MapKeyColumn (name="key")
    @Column(name="value")
    private Map<String,String> properties;

}
