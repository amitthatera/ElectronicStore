package com.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.Images;

public interface ImageRepository extends JpaRepository<Images, Long> {

}
