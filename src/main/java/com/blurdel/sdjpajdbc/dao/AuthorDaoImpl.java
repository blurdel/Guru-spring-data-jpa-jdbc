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
				return getAuthor(rs);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				closeAll(rs, pstmt, conn);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Author getByName(String firstName, String lastName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("select * from author where first_name = ? and last_name = ?");
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return getAuthor(rs);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				closeAll(rs, pstmt, conn);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return null;		
	}

	private Author getAuthor(ResultSet rs) throws SQLException {
		Author author = new Author();
		author.setId(rs.getLong("id"));
		author.setFirstName(rs.getString("first_name"));
		author.setLastName(rs.getString("last_name"));
		return author;
	}

	private void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn) throws SQLException {
		if (rs != null)
			rs.close();
		if (pstmt != null)
			pstmt.close();
		if (conn != null)
			conn.close();
	}
	
}
