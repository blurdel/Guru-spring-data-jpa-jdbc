package com.blurdel.sdjpajdbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blurdel.sdjpajdbc.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
