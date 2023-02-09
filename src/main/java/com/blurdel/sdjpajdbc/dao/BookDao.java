package com.blurdel.sdjpajdbc.dao;

import com.blurdel.sdjpajdbc.domain.Book;

public interface BookDao {

	Book getById(Long id);
	
	Book getByTitle(String title);
	
	Book saveNew(Book book);
	
	Book update(Book book);
	
	void delete(Long id);
	
}
