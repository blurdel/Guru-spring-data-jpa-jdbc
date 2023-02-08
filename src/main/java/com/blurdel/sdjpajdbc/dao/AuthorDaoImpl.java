package com.blurdel.sdjpajdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("select * from author where id=?");
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
			pstmt = conn.prepareStatement("select * from author where first_name=? and last_name=?");
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

	@Override
	public Author saveNew(Author author) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("insert into author (first_name, last_name) values (?,?)");
			pstmt.setString(1, author.getFirstName());
			pstmt.setString(2, author.getLastName());
			pstmt.execute();

			// Return inserted author
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("select LAST_INSERT_ID()");
			if (rs.next()) {
				Long savedId = rs.getLong(1);
				return this.getById(savedId);
			}
			stmt.close();
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
	public Author update(Author author) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("update author set first_name=?, last_name=? where id=?");
			pstmt.setString(1, author.getFirstName());
			pstmt.setString(2, author.getLastName());
			pstmt.setLong(3, author.getId());
			pstmt.execute();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				closeAll(null, pstmt, conn);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return this.getById(author.getId());
	}

	@Override
	public void delete(Long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {

			conn = source.getConnection();
			pstmt = conn.prepareStatement("delete from author where id=?");
			pstmt.setLong(1, id);
			pstmt.execute();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				closeAll(null, pstmt, conn);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
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
