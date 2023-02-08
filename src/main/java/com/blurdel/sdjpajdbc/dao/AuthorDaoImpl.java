package com.blurdel.sdjpajdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("select * from author where id = ?");
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			
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
		finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}

}
