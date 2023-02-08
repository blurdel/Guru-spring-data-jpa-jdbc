package com.blurdel.sdjpajdbc.dao;

import com.blurdel.sdjpajdbc.domain.Author;

public interface AuthorDao {

	Author getById(Long id);
	
	Author getByName(String firstName, String lastName);
	
	Author saveNew(Author author);
	
	Author update(Author author);
	
	void delete(Long id);
	
}
