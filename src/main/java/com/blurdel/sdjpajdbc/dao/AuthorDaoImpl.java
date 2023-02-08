package com.blurdel.sdjpajdbc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.blurdel.sdjpajdbc.domain.Author;

@Component
public class AuthorDaoImpl implements AuthorDao {

	private final DataSource source;
	
	
	public AuthorDaoImpl(DataSource source) {
		this.source = source;
	}


	@Override
	public Author getById(Long id) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from author where id = " + id);
			
			if (rs.next()) {
				Author author = new Author();
				author.setId(id);
				author.setFirstName(rs.getString("first_name"));
				author.setLastName(rs.getString("last_name"));
				return author;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
