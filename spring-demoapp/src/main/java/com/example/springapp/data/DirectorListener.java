package com.example.springapp.data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class DirectorListener {

    @PrePersist
    public void onCreate(Object target) {
        // TODO
    }

    @PreUpdate
    public void onUpdate(Object target) {
        // TODO
    }
}
