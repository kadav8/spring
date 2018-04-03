package com.example.simple;

import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringSimpleExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSimpleExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner dummy(PeopleRepository peopleRepository) {
		return args -> {
			Stream.of("Harry", "Vera", "Emma", "Greg")
					.forEach(name -> peopleRepository.save(new People(name)));
			peopleRepository.findAll().forEach(System.out::println);
		};
	}
}

@RestController
class BasicRestController {

	@Value("${com.example.greeting:Default Hello Spring}")
    String greeting;

	@GetMapping("/hello")
	String hello() {
		return greeting;
	}
}

@RepositoryRestResource
interface PeopleRepository extends JpaRepository<People, Long> {

	@RestResource(path = "byName")
	Collection<People> findByName(@Param("name") String name);
}

@Entity
class People {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public People() {
	}

	public People(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "People [id=" + id + ", name=" + name + "]";
	}
}
