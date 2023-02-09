package com.blurdel.sdjpajdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import com.blurdel.sdjpajdbc.domain.Book;

@Component
public class BookDaoImpl implements BookDao {

	private final DataSource source;
	private final AuthorDao authorDao;
	
	
	public BookDaoImpl(DataSource source, AuthorDao authorDao) {
		this.source = source;
		this.authorDao = authorDao;
	}


	@Override
	public Book getById(Long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("select * from book where id=?");
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return getBook(rs);
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
	public Book getByTitle(String title) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("select * from book where title=?");
			pstmt.setString(1, title);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return getBook(rs);
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
	public Book saveNew(Book book) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("insert into book (isbn, publisher, title, author_id) values (?,?,?,?)");
			pstmt.setString(1, book.getIsbn());
			pstmt.setString(2, book.getPublisher());
			pstmt.setString(3, book.getTitle());
			
			if (book.getAuthor() != null) {
				pstmt.setLong(4, book.getAuthor().getId());
			} else {
				pstmt.setNull(4, Types.BIGINT);
			}
			pstmt.execute();

			// Return inserted book
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
	public Book update(Book book) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			conn = source.getConnection();
			pstmt = conn.prepareStatement("update book set isbn=?, publisher=?, title=?, author_id=? where id=?");
			pstmt.setString(1, book.getIsbn());
			pstmt.setString(2, book.getPublisher());
			pstmt.setString(3, book.getTitle());
			
			if (book.getAuthor() != null) {
				pstmt.setLong(4, book.getAuthor().getId());
			}
			pstmt.setLong(5, book.getId());
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
		return this.getById(book.getId());
	}

	@Override
	public void delete(Long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {

			conn = source.getConnection();
			pstmt = conn.prepareStatement("delete from book where id=?");
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
	
	private Book getBook(ResultSet rs) throws SQLException {
		Book book = new Book();
		book.setId(rs.getLong("id"));
		book.setIsbn(rs.getString("isbn"));
		book.setPublisher(rs.getString("publisher"));
		book.setTitle(rs.getString("title"));
		// Lookup author by id
		book.setAuthor(authorDao.getById(rs.getLong("author_id")));
		return book;
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
