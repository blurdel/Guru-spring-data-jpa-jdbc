package com.blurdel.sdjpajdbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blurdel.sdjpajdbc.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
