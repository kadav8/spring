package com.example.springapp.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {

    public Director findOneByName(String name);
}
