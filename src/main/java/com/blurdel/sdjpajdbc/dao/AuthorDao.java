package com.blurdel.sdjpajdbc.dao;

import com.blurdel.sdjpajdbc.domain.Author;

public interface AuthorDao {

	Author getById(Long id);
	Author getByName(String firstName, String lastName);
	
}
